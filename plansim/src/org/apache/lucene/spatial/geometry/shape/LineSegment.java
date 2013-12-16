/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.lucene.spatial.geometry.shape;


/**
 * 2d line segment.
 *
 * <p><font color="red"><b>NOTE:</b> This API is still in
 * flux and might change in incompatible ways in the next
 * release.</font>
 */
public class LineSegment {
  public final Point2D A = new Point2D();
  public final Point2D B = new Point2D();

  public LineSegment() {
    A.set(0, 0);
    B.set(0, 0);
  }

  public LineSegment(Point2D p1, Point2D p2) {
    A.set(p1);
    B.set(p2);
  }
  
  public LineSegment(double x1, double y1, double x2, double y2)
  {
	  A.set(x1, y1);
	  B.set(x2, y2);
  }

  /**
   * Finds the distance of a specified point from the line segment and the
   * closest point on the segment to the specified point.
   * 
   * @param P
   *            Test point.
   * @param closestPt
   *            (Return) Closest point on the segment to c.
   * 
   * @return Returns the distance from P to the closest point on the segment.
   */
  public double distance(Point2D P, Point2D /* out */closestPt) {
    if (closestPt == null)
      closestPt = new Point2D();

    // Construct vector v (AB) and w (AP)
    Vector2D v = new Vector2D(A, B);
    Vector2D w = new Vector2D(A, P);

    // Numerator of the component of w onto v. If <= 0 then A
    // is the closest point. By separating into the numerator
    // and denominator of the component we avoid a division unless
    // it is necessary.
    double n = w.dot(v);
    if (n <= 0.0f) {
      closestPt.set(A);
      return w.norm();
    }

    // Get the denominator of the component. If the component >= 1
    // (d <= n) then point B is the closest point
    double d = v.dot(v);
    if (d <= n) {
      closestPt.set(B);
      return new Vector2D(B, P).norm();
    }

    // Closest point is along the segment. The point is the projection of
    // w onto v.
    closestPt.set(v.mult(n / d));
    closestPt.add(A);
    return new Vector2D(closestPt, P).norm();
  }
  /**
  * Computes the intersection between two lines. The calculated point is approximate, 
  * since integers are used. If you need a more precise result, use doubles
  * everywhere. 
  * (c) 2007 Alexander Hristov. Use Freely (LGPL license). http://www.ahristov.com
  */
  
  public Point2D intersect(LineSegment seg)
  {
	  double term1 = this.A.x - this.B.x;
	  double term2 = seg.A.y - seg.B.y;
	  double term3 = this.A.y - this.B.y;
	  double term4 = seg.A.x - seg.B.x;
	  
	  if(!java.awt.geom.Line2D.linesIntersect(
			  this.A.x, this.A.y, this.B.x, this.B.y,
			  seg.A.x, seg.A.y, seg.B.x, seg.B.y))
	  {
		  return null;
	  }
	  
	  double d = term1 * term2 - term3 * term4;
	  if (d == 0) return null;
	        
	  double xi = ((seg.A.x-seg.B.x)*(this.A.x*this.B.y-this.A.y*this.B.x)
			  -(this.A.x-this.B.x)*(seg.A.x*seg.B.y-seg.A.y*seg.B.x))/d;
	  double yi = ((seg.A.y-seg.B.y)*(this.A.x*this.B.y-this.A.y*this.B.x)
			  -(this.A.y-this.B.y)*(seg.A.x*seg.B.y-seg.A.y*seg.B.x))/d;
	        
	  return new Point2D(xi,yi);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((A == null) ? 0 : A.hashCode());
    result = prime * result + ((B == null) ? 0 : B.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    LineSegment other = (LineSegment) obj;
    if (A == null) {
      if (other.A != null)
        return false;
    } else if (!A.equals(other.A))
      return false;
    if (B == null) {
      if (other.B != null)
        return false;
    } else if (!B.equals(other.B))
      return false;
    return true;
  }
  
}
