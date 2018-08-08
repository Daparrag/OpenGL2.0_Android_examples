 package opengl_app1.javacodegeeks.com.opengl_app1;



 import java.nio.ByteBuffer;
 import java.nio.ByteOrder;
 import java.nio.FloatBuffer;
 import android.opengl.GLES20;


public class Triangle {

    private FloatBuffer vertexBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private final int BYTES_PER_ELEMENT = 4;



    final float triangleVertices []=
            {//X,Y,Z              //R,G,B,A
                    0.0f, 0.5f,	0.0f,
                     1.0f, 1.0f, 0.0f, 1.0f,

                    -0.5f, -0.5f, 0.0f,
                    0.0f, 0.0f, 1.0f, 1.0f,

                    0.5f, -0.5f, 0.0f,
                    0.5f,  -0.31f, 0.0f, 1.0f
            }; //array not yet used by the graphics card





    private final String vertexShaderCode =
                    "attribute vec4 vertPosition;     \n"     // Per-vertex position information we will pass in.
                    + "attribute vec4 vertColor;        \n"     // Per-vertex color information we will pass in.

                    + "varying vec4 v_Color;          \n"     // This will be passed into the fragment shader.

                    + "void main()                    \n"     // The entry point for our vertex shader.
                    + "{                              \n"
                    + "   v_Color = vertColor;          \n"     // Pass the color through to the fragment shader.
                    // It will be interpolated across the triangle.
                    + "   gl_Position = vertPosition;\n"     // gl_Position is a special variable used to store the final position.
                    + "\n"     // Multiply the vertex by the matrix to get the final point in
                    + "}                              \n";    // normalized screen coordinates.

    private final String fragmentShaderCode =

            "precision mediump float;       \n"     // Set the default precision to medium. We don't need as high of a
                    // precision in the fragment shader.
                    + "varying vec4 v_Color;          \n"     // This is the color from the vertex shader interpolated across the
                    // triangle per fragment.
                    + "void main()                    \n"     // The entry point for our fragment shader.
                    + "{                              \n"
                    + "   gl_FragColor = v_Color;     \n"     // Pass the color directly through the pipeline.
                    + "}                              \n";


    public Triangle(){
        int vertexShader =MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);


        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        // Bind the vertex shader to the program.
        GLES20.glBindAttribLocation(mProgram,0,"vertPosition");
        GLES20.glBindAttribLocation(mProgram,1,"vertColor");


        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables


        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                triangleVertices.length * 4);

        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();

        vertexBuffer.put(triangleVertices);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);

    }



    public void draw(float[] mvpMatrix) {

        // get handle to vertex shader's vPosition member

        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vertPosition");
        vertexBuffer.position(0);
        //layout of the the attribute
        GLES20.glVertexAttribPointer(
                mPositionHandle,
                3,
                GLES20.GL_FLOAT,
                false,
                7 * BYTES_PER_ELEMENT,
                vertexBuffer
        );


        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetAttribLocation(mProgram, "vertColor");
        // Set color for drawing the triangle
        //GLES20.glUniform3fv(mColorHandle, 1, tmp_color, 0);
        vertexBuffer.position(3);
        GLES20.glVertexAttribPointer(
                mColorHandle,
                4,
                GLES20.GL_FLOAT,
                false,
                7 * BYTES_PER_ELEMENT,
                vertexBuffer
        );

        GLES20.glEnableVertexAttribArray(mColorHandle);

        // Apply the projection and view transformation
       // GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        //mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "u_MVPMatrix");





        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,3);
        // Disable vertex array
        //GLES20.glDisableVertexAttribArray(mPositionHandle);

    }

}

