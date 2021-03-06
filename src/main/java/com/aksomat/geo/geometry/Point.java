package com.aksomat.geo.geometry;

import com.aksomat.geo.utils.Model;

/** A point with a latitude and longitude */
public class Point {
  private final double lat;
  private final double lon;
  private String description = null;

  /**
   * Constructs a 2-D Point with a latitude and a longitude
   *
   * @param lat the latitude
   * @param lon the longitude
   */
  public Point(double lat, double lon) {
    // FIXME Validate paramaters within [-90, 90]
    this.lat = lat;
    this.lon = lon;
  }

  public Point(double lat, double lon, String description) {
    // TODO Validate arguments and throw IllegalArgumentException
    this.lat = lat;
    this.lon = lon;
    this.description = description;
  }

  /**
   * Given an initial point, an azimuth and a surface distance; calculates the corresponding end
   * point.
   *
   * @param startPoint the initial point
   * @param azimuth the direction (angle) clockwise relative to north in radians
   * @param distance the surface distance.
   * @return
   */
  public static Point greatCirclePoint(Point startPoint, double azimuth, double distance) {
    // TODO Should use n-vector instead of Lat, Lon to avoid common problems
    double coLatStartRAD = Math.toRadians(90 - startPoint.getLat());
    double bearingRAD = azimuth < 180 ? Math.toRadians(azimuth) : Math.toRadians(360 - azimuth);
    double radianDistance = distance / Model.EARTH_MEAN_RADIUS;
    double coLatPointRAD =
        Model.ahav(Model.lawOfHaversines(radianDistance, coLatStartRAD, bearingRAD));
    // FIXME Singularity at north pole. Use n-vector instead
    double lonDiffAbs =
        Math.toDegrees(
            Model.ahav(
                (Model.hav(radianDistance) - Model.hav(coLatStartRAD - coLatPointRAD))
                    / (Math.sin(coLatStartRAD) * Math.sin(coLatPointRAD))));
    double lon =
        azimuth < 180 ? startPoint.getLon() + lonDiffAbs : startPoint.getLon() - lonDiffAbs;
    double lat = 90 - Math.toDegrees(coLatPointRAD);
    Point point = new Point(lat, lon);
    return point;
  }

  @Override
  public String toString() {
    String strLat = String.format("%.6f", lat);
    String strLon = String.format("%.6f", lon);
    if (description != null && !description.isBlank()) {
      return "Point{Lon=" + strLon + ", Lat=" + strLat + ", Description: " + description + "}";
    } else {
      return "Point{Lon=" + strLon + ", Lat=" + strLat + "}";
    }
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Get the latitude of a point in degrees
   *
   * @return the latitude
   */
  public double getLat() {
    return lat;
  }

  /**
   * Get the longitude of a point in degrees
   *
   * @return the longitude
   */
  public double getLon() {
    return lon;
  }

  /**
   * Get the point values as an array
   *
   * @return the latitude and longitude in an array where the first item is the latitude, and the
   *     second is the longitude
   */
  public double[] getArray() {
    double[] points = {lat, lon};
    return points;
  }
}
