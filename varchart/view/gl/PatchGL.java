package varchart.view.gl;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import varchart.view.MouseableGLWindow;
import varchart.view.PatchView;

public class PatchGL extends MouseableGLWindow implements GLEventListener {
	GLU glu;
    
    //handles for compiled GL shapes
    int patchTileListHandle, patchThickTileListHandle, patchStickListHandle, sphereDotListHandle, altThickPatchHandle, axisHeadHandle, patchDiskTileHandle, patchSkyscraperHandle;

    boolean sticks = false;
    boolean tangents = false; //true;
    
    public PatchGL(PatchView parent) {
    	super(parent);
    }
    
    private void setupCompiliedDisplayLists(GL gl) {
		 patchTileListHandle = gl.glGenLists(1);
		 gl.glNewList(patchTileListHandle, GL.GL_COMPILE);
		 Compilables.PatchTile(gl);
		 gl.glEndList();
		 
		 patchStickListHandle = gl.glGenLists(1);
		 gl.glNewList(patchStickListHandle, GL.GL_COMPILE);
		 Compilables.PatchStickTile(gl, 10 );
		 gl.glEndList();
		 
		 patchThickTileListHandle = gl.glGenLists(1);
		 gl.glNewList(patchThickTileListHandle, GL.GL_COMPILE);
		 Compilables.ThickPatchTile(gl, .4f, .2f);
		 gl.glEndList();
		 
		 sphereDotListHandle = gl.glGenLists(1);
		 GLUquadric quadr = glu.gluNewQuadric();
		 glu.gluQuadricDrawStyle(quadr, GLU.GLU_FILL);
		 glu.gluQuadricNormals(quadr, GLU.GLU_SMOOTH);
		 final float radius = 0.4f;
		 final int slices = 16;
		 final int stacks = 16;
		 gl.glNewList(sphereDotListHandle, GL.GL_COMPILE);
		 Compilables.Sphere(gl, glu, quadr, radius, slices, stacks);
		 gl.glEndList();
		 
		 
		 altThickPatchHandle = gl.glGenLists(1);
		 gl.glNewList(altThickPatchHandle, GL.GL_COMPILE);
		 Compilables.ThickPatchTile(gl, .4f, .15f);
		 Compilables.Sphere(gl, glu, quadr, 0.3f, slices, stacks);
		 gl.glEndList();
		 
		 patchSkyscraperHandle = gl.glGenLists(1);
		 gl.glNewList(patchSkyscraperHandle, GL.GL_COMPILE);
		 Compilables.box(gl, .2f, 1.0f);
		 gl.glEndList();
		 
		 axisHeadHandle = gl.glGenLists(1);
		 gl.glNewList(axisHeadHandle, GL.GL_COMPILE);
		 Compilables.AxisHead(gl, glu, quadr, 1.3, stacks);
		 gl.glEndList();
		 
		 patchDiskTileHandle = gl.glGenLists(1);
		 gl.glNewList(patchDiskTileHandle, GL.GL_COMPILE);
		 Compilables.DiskPatchTile(gl, glu, quadr, 0.4, stacks);
		 gl.glEndList();
		 
		 glu.gluDeleteQuadric(quadr);
	}
    
    
    @Override
	public void init(GLAutoDrawable drawable) {
    	GL gl = drawable.getGL();
    	glu = new GLU();
    	setupCompiliedDisplayLists( gl );
    	setupLightingAndViewPort(gl, glu);
	}

    
    @Override
	public void display(GLAutoDrawable drawable) {
    	GL gl = drawable.getGL();
    	gl.glMatrixMode( GL.GL_MODELVIEW );
		gl.glLoadIdentity();
		
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);	
		gl.glLineWidth(1.0f);

		//gl.glColor3f(1.0f, 0.6f, 5.9f);
		gl.glColor3f(0.2f, 0.2f, 1.9f);
		gl.glPushMatrix();
		observer.applyPerspective(gl);
		for (int i=0; i<myViewer.worldWidth; i++) {
			for (int j = 0; j<myViewer.worldHeight; j++) {
				gl.glPushMatrix();
				gl.glTranslated(i + myViewer.minPxcor, j + myViewer.minPycor, -0.01);
				gl.glCallList(patchTileListHandle);
				gl.glPopMatrix();
			}
		}

		for (int i=0; i<myViewer.worldWidth; i++) {
			for (int j = 0; j<myViewer.worldHeight; j++) {
				gl.glPushMatrix();

				double val = myViewer.zScale * ((PatchView)myViewer).reporterValueMatrix[i][j];
				gl.glTranslated(i + myViewer.minPxcor, j + myViewer.minPycor,val);

				if (sticks) {
					gl.glColor3f(2.5f, 2.5f, 2.5f);
					gl.glLineWidth(0.1f);
					gl.glBegin (GL.GL_LINES);
					gl.glVertex3i (0, 0, 0);
					gl.glVertex3d (0, 0, -val);
					gl.glEnd();
					
				}
				
				if ( tangents ) {
					gl.glColor3f(1.0f, 3.9f, 0.6f);
					if ( j>0 && j<myViewer.worldHeight-1  ) {
						double slopey = (((PatchView)myViewer).reporterValueMatrix[i][j+1] - ((PatchView)myViewer).reporterValueMatrix[i][j-1] ) / 2.0 ;
						double beta = 180.0 * Math.atan(slopey) / Math.PI;
						gl.glRotated(beta, 1, 0, 0);
					}
					if ( i>0 && i<myViewer.worldWidth-1  ) {
						double slopex = (((PatchView)myViewer).reporterValueMatrix[i+1][j] - ((PatchView)myViewer).reporterValueMatrix[i-1][j] ) / 2.0 ;
						double alpha = 180.0 * Math.atan(slopex) / Math.PI;
						gl.glRotated(-alpha, 0, 1, 0);
					}
					gl.glCallList(patchDiskTileHandle);
				} else {
					//gl.glColor3f(1.0f, 3.9f, 0.6f);
					gl.glColor3f(0.1f, 0.8f, 0.1f);
					gl.glTranslated(0, 0, -val);
					gl.glScaled(1, 1, val);
					
//					float[] rgba = {0.3f, 1f, 0.2f};
//			        gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, rgba, 0);
//			        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, rgba, 0);
//			        gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 0.5f);
			        
					gl.glCallList(patchSkyscraperHandle);
					//gl.glCallList(sphereDotListHandle);
				}
				//gl.glCallList(altThickPatchHandle);
				gl.glPopMatrix();
			}
		}
		drawAxesIfDragging(gl, axisHeadHandle);
		gl.glPopMatrix();
    }

    
    
    @Override
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
    		boolean deviceChanged) {
    }


    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
    		int height) {
    }
	
}
