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
    private final int BYTES_PER_ELEMENT_FLOAT = 4;
    private final int BYTES_PER_ELEMENT_SHORT = 2;


    final float cubeVertices [] = {

            //X,Y,Z 			    R,G,B
            // Top
             -1.0f, 1.0f, -1.0f,   1.0f, 1.0f, 0.0f,
             -1.0f, 1.0f, 1.0f,    0.7f, 0.5f, 1.0f,
             1.0f, 1.0f, 1.0f,     0.1f, 0.5f, 0.5f,
             1.0f, 1.0f, -1.0f,    0.5f, 1.0f, 0.6f,


            // Left
            -1.0f, 1.0f, 1.0f,    1.0f, 1.0f, 0.0f,
            -1.0f, -1.0f, 1.0f,   0.7f, 0.5f, 1.0f,
            -1.0f, -1.0f, -1.0f,  0.1f, 0.5f, 0.5f,
            -1.0f, 1.0f, -1.0f,   0.5f, 1.0f, 0.6f,


            // Right
            1.0f, 1.0f, 1.0f,    1.0f, 1.0f, 0.0f,
            1.0f, -1.0f, 1.0f,   0.7f, 0.5f, 1.0f,
            1.0f, -1.0f, -1.0f,  0.1f, 0.5f, 0.5f,
            1.0f, 1.0f, -1.0f,   0.5f, 1.0f, 0.6f,

            // Front
            1.0f, 1.0f, 1.0f,    1.0f, 1.0f, 0.0f,
            1.0f, -1.0f, 1.0f,    0.7f, 0.5f, 1.0f,
           -1.0f, -1.0f, 1.0f,    0.1f, 0.5f, 0.5f,
           -1.0f, 1.0f, 1.0f,    0.5f, 1.0f, 0.6f,

            // Back
            1.0f, 1.0f, -1.0f,    1.0f, 1.0f, 0.0f,
            1.0f, -1.0f, -1.0f,    0.7f, 0.5f, 1.0f,
           -1.0f, -1.0f, -1.0f,    0.1f, 0.5f, 0.5f,
           -1.0f, 1.0f, -1.0f,    0.5f, 1.0f, 0.6f,

            // Bottom
            -1.0f, -1.0f, -1.0f,   1.0f, 1.0f, 0.0f,
            -1.0f, -1.0f, 1.0f,    0.7f, 0.5f, 1.0f,
            1.0f, -1.0f, 1.0f,     0.1f, 0.5f, 0.5f,
            1.0f, -1.0f, -1.0f,    0.5f, 1.0f, 0.6f

    };//array not yet used by the graphics card


    /*cube indices*/

    final short cubeindices[] =
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



    private final String vertexShaderCode =
            "precision mediump float;       \n"
            +""
            +"attribute vec3 vertPosition;  \n"
            +"attribute vec3 vertColor;     \n"
            +"varying vec3 fragColor;       \n"
            +"\n"
            +"uniform mat4 mWorld;          \n"
            +"uniform mat4 mView;           \n"
            +"uniform mat4 mProj;           \n"
            +"void main ()                  \n"
            +"{\n"
            +"fragColor = vertColor;        \n"
            +"gl_Position = mProj * mView * mWorld *  vec4(vertPosition, 1.0);\n"
            +"\n"
            +"}                             \n";


    private final String fragmentShaderCode =
            "precision mediump float;       \n"
            +"\n"
            +"varying vec3 fragColor;       \n"
            +"void main()                   \n"
            +"{                             \n"
            +"gl_FragColor = vec4(fragColor, 1.0);\n"
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

    }



    public void draw(float[] mvpMatrix){

        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vertPosition");

        GLES20.glVertexAttribPointer(
                mPositionHandle,
                3,
                GLES20.GL_FLOAT,
                false,
                6*BYTES_PER_ELEMENT_FLOAT,
                vertexBuffer
        );

        GLES20.glEnableVertexAttribArray(mPositionHandle);





    }


}
