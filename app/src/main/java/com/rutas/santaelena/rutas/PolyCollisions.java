/*
package com.rutas.santaelena.rutas;
 */

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Javier on 09/11/2017.
 * https://stackoverflow.com/questions/19048122/how-to-get-the-nearest-point-outside-a-polygon-from-a-point-inside-a-polygon/45123687#45123687
 */
/*
public class PolyCollisions {
    // Call this function...
     static LatLng doCollisions (List<LatLng> polygon, LatLng point) {

        if(!pointIsInPoly(polygon, point)) {
            // The point is not colliding with the polygon, so it does not need to change location
            return point;
        }

        // Get the closest point off the polygon
        return closestPointOutsidePolygon(polygon, point);

    }

    // Check if the given point is within the given polygon (Vertexes)
    //
    // If so, call on collision if required, and move the point to the
    // closest point outside of the polygon
    public static boolean pointIsInPoly(List<LatLng> verts, LatLng p) {
        int nvert = verts.size();
        double[] vertx = new double[nvert];
        double[] verty = new double[nvert];
        for(int i = 0; i < nvert; i++) {
            LatLng vert = verts.get(i);
            vertx[i] = vert.latitude;
            verty[i] = vert.longitude;
        }
        double testx = p.latitude;
        double testy = p.longitude;
        int i, j;
        boolean c = false;
        for (i = 0, j = nvert-1; i < nvert; j = i++) {
            if ( ((verty[i]>testy) != (verty[j]>testy)) &&
                    (testx < (vertx[j]-vertx[i]) * (testy-verty[i]) / (verty[j]-verty[i]) + vertx[i]) )
                c = !c;
        }
        return c;
    }

    // Gets the closed point that isn't inside the polygon...
    public static LatLng closestPointOutsidePolygon (List<LatLng> poly, LatLng point) {

        return getClosestPointInSegment(closestSegment(poly, point), point);

    }

    public static LatLng getClosestPointInSegment (List<LatLng> segment, LatLng point) {

        return newPointFromCollision(segment.get(0), segment.get(1), point);

    }

    public static LatLng newPointFromCollision (LatLng aLine, LatLng bLine, LatLng p) {

        return nearestPointOnLine(aLine.latitude, aLine.longitude, bLine.latitude, bLine.longitude, p.latitude, p.longitude);


    }

    public static LatLng nearestPointOnLine(double ax, double ay, double bx, double by, double px, double py) {

        // https://stackoverflow.com/questions/1459368/snap-point-to-a-line-java

        double apx = px - ax;
        double apy = py - ay;
        double abx = bx - ax;
        double aby = by - ay;

        double ab2 = abx * abx + aby * aby;
        double ap_ab = apx * abx + apy * aby;
        double t = ap_ab / ab2;
        if (t < 0) {
            t = 0;
        } else if (t > 1) {
            t = 1;
        }
        return new LatLng(ax + abx * t, ay + aby * t);
    }

    public static List<LatLng> closestSegment (List<LatLng> points, LatLng point) {

        List<LatLng> returns = new List<LatLng>;

        int index = closestPointIndex(points, point);

        final LatLng set = returns.set(0, points.get(index));

        List<LatLng> neighbors = new List<LatLng> {
                points.get((index + 1 + points.size()) % points.size()),
                        points.get((index - 1 + points.size()) % points.size())
        };

        double[] neighborAngles = new double[] {
                getAngle(new List<LatLng> point, returns.get(0), neighbors.get(0)),
                getAngle(new List<LatLng> point, returns.get(0), neighbors.get(1))
        };

        if(neighborAngles[0] < neighborAngles[1]) {
            returns.set(1, neighbors.get(0));
        } else {
            returns.set(1, neighbors.get(0));
        }

        return returns;

    }


    public static double getAngle (List<LatLng> abc) {

        // https://stackoverflow.com/questions/1211212/how-to-calculate-an-angle-from-three-points
        // atan2(P2.y - P1.y, P2.x - P1.x) - atan2(P3.y - P1.y, P3.x - P1.x)
        //return Math.atan2(abc[2].y - abc[0].y, abc[2].x - abc[0].x) - Math.atan2(abc[1].y - abc[0].y, abc[1].x - abc[0].x);

        return Math.atan2(abc.get(2).longitude - abc.get(0).longitude, abc.get(2).latitude - abc.get(0).latitude) - Math.atan2(abc.get(1).longitude - abc.get(0).longitude, abc.get(1).latitude - abc.get(0).latitude);

    }

    public static int closestPointIndex (List<LatLng> points, LatLng point) {

        int leastDistanceIndex = 0;
        double leastDistance = Double.MAX_VALUE;

        for(int i = 0; i < points.size(); i++) {
            double dist = distance(points.get(i), point);
            if(dist < leastDistance) {
                leastDistanceIndex = i;
                leastDistance = dist;
            }
        }

        return leastDistanceIndex;

    }

    public static double distance (LatLng a, LatLng b) {
        return Math.sqrt(Math.pow(Math.abs(a.latitude-b.latitude), 2)+Math.pow(Math.abs(a.longitude-b.longitude), 2));
    }

}

*/