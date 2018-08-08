package opengl_app1.javacodegeeks.com.opengl_app1;

import android.opengl.GLES20;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


import javax.microedition.khronos.opengles.GL;

public class Cube {

    //define buffers

    private FloatBuffer vertexBuffer;
    private ShortBuffer indexBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private final int BYTES_PER_ELEMENT_FLOAT = 4;
    private final int BYTES_PER_ELEMENT_SHORT = 2;


    final float cubeVertices [] = {

            //X,Y,Z 			    R,G,B
            // Top
            -1.0f, 1.0f, -1.0f,   1.0f, 1.0f, 0.0f,1.0f,
            -1.0f, 1.0f, 1.0f,    0.7f, 0.5f, 1.0f,1.0f,
            1.0f, 1.0f, 1.0f,     0.1f, 0.5f, 0.5f,1.0f,
            1.0f, 1.0f, -1.0f,    0.5f, 1.0f, 0.6f,1.0f,


            // Left
            -1.0f, 1.0f, 1.0f,    1.0f, 1.0f, 0.0f,1.0f,
            -1.0f, -1.0f, 1.0f,   0.7f, 0.5f, 1.0f,1.0f,
            -1.0f, -1.0f, -1.0f,  0.1f, 0.5f, 0.5f,1.0f,
            -1.0f, 1.0f, -1.0f,   0.5f, 1.0f, 0.6f,1.0f,


            // Right
            1.0f, 1.0f, 1.0f,    1.0f, 1.0f, 0.0f,1.0f,
            1.0f, -1.0f, 1.0f,   0.7f, 0.5f, 1.0f,1.0f,
            1.0f, -1.0f, -1.0f,  0.1f, 0.5f, 0.5f,1.0f,
            1.0f, 1.0f, -1.0f,   0.5f, 1.0f, 0.6f,1.0f,

            // Front
            1.0f, 1.0f, 1.0f,    1.0f, 1.0f, 0.0f,1.0f,
            1.0f, -1.0f, 1.0f,    0.7f, 0.5f, 1.0f,1.0f,
            -1.0f, -1.0f, 1.0f,    0.1f, 0.5f, 0.5f,1.0f,
            -1.0f, 1.0f, 1.0f,    0.5f, 1.0f, 0.6f,1.0f,

            // Back
            1.0f, 1.0f, -1.0f,    1.0f, 1.0f, 0.0f,1.0f,
            1.0f, -1.0f, -1.0f,    0.7f, 0.5f, 1.0f,1.0f,
            -1.0f, -1.0f, -1.0f,    0.1f, 0.5f, 0.5f,1.0f,
            -1.0f, 1.0f, -1.0f,    0.5f, 1.0f, 0.6f,1.0f,

            // Bottom
            -1.0f, -1.0f, -1.0f,   1.0f, 1.0f, 0.0f,1.0f,
            -1.0f, -1.0f, 1.0f,    0.7f, 0.5f, 1.0f,1.0f,
            1.0f, -1.0f, 1.0f,     0.1f, 0.5f, 0.5f,1.0f,
            1.0f, -1.0f, -1.0f,    0.5f, 1.0f, 0.6f,1.0f,

    };//array not yet used by the graphics card


    /*cube indices*/

    final  short cubeindices[] =
            {
                    //indicate which set of vertices form the triangles
                    // form each face of the cube


                    // Top
                    0, 1, 2,
                    0, 2, 3,

                    // Left
                    5, 4, 6,
                    6, 4, 7,

                    // Right
                    8, 9, 10,
                    8, 10, 11,

                    // Front
                    13, 12, 14,
                    15, 14, 12,

                    // Back
                    16, 17, 18,
                    16, 18, 19,


                    // Bottom
                    21, 20, 22,
                    22, 20, 23
            };


//    private final String vertexShaderCode =
//            "attribute vec4 vertPosition;     \n"     // Per-vertex position information we will pass in.
//                    + "attribute vec4 vertColor;        \n"     // Per-vertex color information we will pass in.
//
//                    + "varying vec4 v_Color;          \n"     // This will be passed into the fragment shader.
//
//                    + "void main()                    \n"     // The entry point for our vertex shader.
//                    + "{                              \n"
//                    + "   v_Color = vertColor;          \n"     // Pass the color through to the fragment shader.
//                    // It will be interpolated across the triangle.
//                    + "   gl_Position = vertPosition;\n"     // gl_Position is a special variable used to store the final position.
//                    + "\n"     // Multiply the vertex by the matrix to get the final point in
//                    + "}                              \n";    // normalized screen coordinates.
//
//    private final String fragmentShaderCode =
//
//            "precision mediump float;       \n"     // Set the default precision to medium. We don't need as high of a
//                    // precision in the fragment shader.
//                    + "varying vec4 v_Color;          \n"     // This is the color from the vertex shader interpolated across the
//                    // triangle per fragment.
//                    + "void main()                    \n"     // The entry point for our fragment shader.
//                    + "{                              \n"
//                    + "   gl_FragColor = v_Color;     \n"     // Pass the color directly through the pipeline.
//                    + "}                              \n";
//


    private final String vertexShaderCode =
            "precision mediump float;       \n"
            +""
            +"attribute vec3 vertPosition;  \n"
            +"attribute vec4 vertColor;     \n"
            +"varying vec4 fragColor;       \n"
            +"\n"
            +"uniform mat4 mWorld;          \n"
            +"uniform mat4 mView;           \n"
            +"uniform mat4 mProj;           \n"
            +"void main ()                  \n"
            +"{\n"
            +"fragColor = vertColor;        \n"
            +"gl_Position = mWorld * vec4(vertPosition, 1.0);\n"
            + "\n"
            +"\n"
            +"}                             \n";


    private final String fragmentShaderCode =
            "precision mediump float;       \n"
            +"\n"
            +"varying vec4 fragColor;       \n"
            +"void main()                   \n"
            +"{                             \n"
            +"gl_FragColor = fragColor;\n"
            +"}                             \n";


    public Cube(){
        //Constructor
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glFrontFace(GLES20.GL_CCW);
        GLES20.glCullFace(GLES20.GL_BACK);


        int vertexShader =MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);


        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to
        // Bind the vertex shader to the program.
        GLES20.glBindAttribLocation(mProgram,0,"vertPosition");
        GLES20.glBindAttribLocation(mProgram,1,"vertColor");
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables

        // allocate cube indices buffer

        vertexBuffer = ByteBuffer.allocateDirect(cubeVertices.length * BYTES_PER_ELEMENT_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuffer.put(cubeVertices).position(0);


        indexBuffer = ByteBuffer.allocateDirect(cubeindices.length * BYTES_PER_ELEMENT_SHORT)
                .order(ByteOrder.nativeOrder()).asShortBuffer();
        indexBuffer.put(cubeindices).position(0);


        //Bin buffer and data
        //GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,0 ,indexBuffer,GLES20.GL_STATIC_DRAW);




    }



    public void draw(float[] mMVPMatrix){

        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vertPosition");
        vertexBuffer.position(0);
        GLES20.glVertexAttribPointer(
                mPositionHandle,
                3,
                GLES20.GL_FLOAT,
                false,
                7*BYTES_PER_ELEMENT_FLOAT,
                vertexBuffer
        );

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetAttribLocation(mProgram, "vertColor");
        vertexBuffer.position(3); //buffer offset
        GLES20.glVertexAttribPointer(
                mColorHandle,
                4,
                GLES20.GL_FLOAT,
                false,
                7*BYTES_PER_ELEMENT_FLOAT,
                vertexBuffer
        );

        GLES20.glEnableVertexAttribArray(mColorHandle);
        GLES20.glUseProgram(mProgram);




        int mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram,"mWorld");

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
                GLES20.glDrawElements(GLES20.GL_TRIANGLES,cubeindices.length,GLES20.GL_UNSIGNED_SHORT,indexBuffer);
                //GLES20.glDisableVertexAttribArray(mPositionHandle);
                //GLES20.glDisableVertexAttribArray(mColorHandle);

    }


}
