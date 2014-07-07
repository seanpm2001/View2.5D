package varchart.view.gl;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

public final class Compilables {

	public static void PatchTile( GL gl) {
		gl.glBegin(GL.GL_POLYGON);
		gl.glVertex2d(-.4, .4);
		gl.glVertex2d(.4, .4);
		gl.glVertex2d(.4, -.4);
		gl.glVertex2d(-.4, -.4);
		gl.glVertex2d(-.4, .4);
		gl.glEnd();
	}

	
	public static void PatchStickTile( GL gl, int stickheight) {
		gl.glColor3f(1.0f, 0.6f, 5.9f);
		gl.glBegin(GL.GL_POLYGON);
		gl.glVertex2d(-.4, .4);
		gl.glVertex2d(.4, .4);
		gl.glVertex2d(.4, -.4);
		gl.glVertex2d(-.4, -.4);
		gl.glVertex2d(-.4, .4);
		gl.glEnd();

		gl.glColor3f(2.5f, 2.5f, 2.5f);
		gl.glLineWidth(0.1f);
		gl.glBegin (GL.GL_LINES);
		gl.glVertex3i (0, 0, 0);
		gl.glVertex3i (0, 0, stickheight);
		gl.glEnd();
	}

	
	//sz should be greater than 0 and less than or equal to  .5
	//depth controls the z-size of the tile
	public static void ThickPatchTile( GL gl, final float sz, final float depth) {
		gl.glBegin(GL.GL_POLYGON);/* f1: front */
		gl.glNormal3f(0,sz,0);
		gl.glVertex3f(-sz,sz,depth);
		gl.glVertex3f(sz,sz,depth);
		gl.glVertex3f(sz,sz,-depth);
		gl.glVertex3f(-sz,sz,-depth);
		gl.glEnd();
		gl.glBegin(GL.GL_POLYGON);/* f2: bottom */
		gl.glNormal3f(0,0,-depth);
		gl.glVertex3f(-sz,sz,-depth);
		gl.glVertex3f(sz,sz,-depth);
		gl.glVertex3f(sz,-sz,-depth);
		gl.glVertex3f(-sz,-sz,-depth);
		gl.glEnd();
		gl.glBegin(GL.GL_POLYGON);/* f3:back */
		gl.glNormal3f(0,-sz,0);
		gl.glVertex3f(-sz,-sz,depth);
		gl.glVertex3f(sz,-sz,depth);
		gl.glVertex3f(sz,-sz,-depth);
		gl.glVertex3f(-sz,-sz,-depth);
		gl.glEnd();
		gl.glBegin(GL.GL_POLYGON);/* f4: top */
		gl.glNormal3f(0,0,depth);
		gl.glVertex3f(-sz,sz,depth);
		gl.glVertex3f(sz,sz,depth);
		gl.glVertex3f(sz,-sz,depth);
		gl.glVertex3f(-sz,-sz,depth);
		gl.glEnd();
		gl.glBegin(GL.GL_POLYGON);/* f5: left */
		gl.glNormal3f(-sz,0,0);
		gl.glVertex3f(-sz,-sz,depth);
		gl.glVertex3f(-sz,sz,depth);
		gl.glVertex3f(-sz,sz,-depth);
		gl.glVertex3f(-sz,-sz,-depth);
		gl.glEnd();
		gl.glBegin(GL.GL_POLYGON);/* f6: right */
		gl.glNormal3f(sz,0,0);
		gl.glVertex3f(sz,-sz,depth);
		gl.glVertex3f(sz,sz,depth);
		gl.glVertex3f(sz,sz,-depth);
		gl.glVertex3f(sz,-sz,-depth);
		gl.glEnd();
	}


	public static void Sphere( GL gl, GLU glu, GLUquadric quadr, float radius, int slices, int stacks ) {
		glu.gluSphere(quadr, radius, slices, stacks);
	}
	
	public static void AxisHead(GL gl, GLU glu, GLUquadric quadr, final double sz, final int smoothness) {
		//gl.glRotatef(-90f, 1f, 0f, 0f);
	    glu.gluCylinder(quadr, sz / 2.0, 0.0, sz, smoothness, smoothness);
	    gl.glRotatef(180f, 1f, 0f, 0f);
	    glu.gluDisk(quadr, 0f, sz / 2, smoothness, 1);
	}

}
