package com.kgc.sauw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kgc.sauw.Vector2d;

public class Camera2D{

    public OrthographicCamera CAMERA;
    public int SIZE,X,Y,ANGLE,W,H;
	
	private boolean isCameraScaling = false;
	private float cameraScale = 0;
	private float cameraScaleSec = 0;
	public float cameraZoom = 1f;
	private float altCameraZoom;
	
	public void setCameraZoom(float zoom){
		CAMERA.zoom = zoom;
		cameraZoom = zoom;
	}
	public void setCameraZoom(float zoom, float sec){
		/*isCameraScaling = true;
		cameraScale = zoom;
		cameraScaleSec = sec / 10;
		altCameraZoom = cameraZoom;*/
        CAMERA.zoom = zoom;
	}
    public void update(SpriteBatch b){
		if(isCameraScaling){
			float temp = (cameraScale - altCameraZoom) / cameraScaleSec * Gdx.graphics.getRawDeltaTime();
			setCameraZoom(cameraZoom + temp);
			Gdx.app.log("temp", temp + "");
            Gdx.app.log("d", Gdx.graphics.getRawDeltaTime() + "");
			if((temp < 0 && cameraZoom <= cameraScale) || (temp > 0 && cameraZoom >= cameraScale)){
				isCameraScaling = false;
				setCameraZoom(cameraScale);
			}
		}
        CAMERA.update();
        b.setProjectionMatrix(CAMERA.combined);
    }

    private void configure(int size){
        int w=Gdx.graphics.getWidth(),
                h=Gdx.graphics.getHeight();

        if( h<w ){
            CAMERA.setToOrtho( false,size,size*h/w );
            W=size;
            H=size*h/w;
        }else{
            CAMERA.setToOrtho( false,size*w/h,size );
            W=size*w/h;
            H=size;
        }

        CAMERA.translate(X,Y);
        CAMERA.rotate(ANGLE);
    }

    public Camera2D(int size){
        CAMERA=new OrthographicCamera();
        configure(size);
        SIZE=size;
    }

    public Camera2D(){
        int size=Math.max(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        CAMERA=new OrthographicCamera();
        configure(size);
        SIZE=size;
    }

    public void rotate(int a){
        CAMERA.rotate(a);
        ANGLE+=a;
    }

    public void setAngle(int a){
        CAMERA.rotate(-ANGLE+a);
        ANGLE=a;
    }

    public void lookAt(int x,int y){
        CAMERA.translate(-X+x,-Y+y);
        X=x;
        Y=y;
    }

    public void translatePosition(int x,int y){
        CAMERA.translate(x,y);
        X+=x;
        Y+=y;
    }

    public void translatePosition(Vector2d v){
        CAMERA.translate(v.x(),v.y());
        X+=v.x();
        Y+=v.y();
    }

    public void resize(int size){
        SIZE=size;
        configure(size);
    }

    public void resize(){
        configure(SIZE);
    }

    public void translateScale(int lss){
        resize(SIZE+lss);
    }

    public int touchX(){
        return Gdx.input.getX()+X;
    }

    public int touchY(){
        return H-Gdx.input.getY()+Y;
    }

    public int touchYI(){
        return Gdx.input.getY()+(H-Y)+H;
    }

    public int touchYI(int i){
        return Gdx.input.getY(i)+(H-Y)+H;
    }

    public int touchX(int i){
        return Gdx.input.getX(i)+X;
    }

    public int touchY(int i){
        return H-Gdx.input.getY(i)+Y;
    }



}