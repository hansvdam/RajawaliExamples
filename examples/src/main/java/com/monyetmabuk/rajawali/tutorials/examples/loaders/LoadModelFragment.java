package com.monyetmabuk.rajawali.tutorials.examples.loaders;

import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import com.monyetmabuk.rajawali.tutorials.R;
import com.monyetmabuk.rajawali.tutorials.examples.*;

import org.rajawali3d.*;
import org.rajawali3d.animation.*;
import org.rajawali3d.cameras.*;
import org.rajawali3d.lights.*;
import org.rajawali3d.loader.*;
import org.rajawali3d.math.vector.*;

public class LoadModelFragment extends AExampleFragment implements
														SeekBar.OnSeekBarChangeListener
{

	private SeekBar mSeekBarX, mSeekBarY, mSeekBarZ;
	private Vector3 mCameraOffset;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mCameraOffset = new Vector3();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		LinearLayout ll = new LinearLayout(getActivity());
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setGravity(Gravity.BOTTOM);

		mSeekBarX = new SeekBar(getActivity());
		mSeekBarX.setMax(100);
		mSeekBarX.setProgress(50);
		mSeekBarX.setOnSeekBarChangeListener(this);
		ll.addView(mSeekBarX);

		mSeekBarY = new SeekBar(getActivity());
		mSeekBarY.setMax(100);
		mSeekBarY.setProgress(50);
		mSeekBarY.setOnSeekBarChangeListener(this);
		ll.addView(mSeekBarY);

		mSeekBarZ = new SeekBar(getActivity());
		mSeekBarZ.setMax(100);
		mSeekBarZ.setProgress(50);
		mSeekBarZ.setOnSeekBarChangeListener(this);
		ll.addView(mSeekBarZ);

		mLayout.addView(ll);

		return mLayout;
	}
	public void onProgressChanged(SeekBar seekBar, int progress,
								  boolean fromUser) {
		mCameraOffset.setAll(mSeekBarX.getProgress()-50,
							 mSeekBarY.getProgress()-100, mSeekBarZ.getProgress()-50);
		((LoadModelRenderer) mRenderer).setCameraOffset(mCameraOffset);
	}

	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	public void onStopTrackingTouch(SeekBar seekBar) {
	}

	@Override
    public AExampleRenderer createRenderer() {
		return new LoadModelRenderer(getActivity());
	}

	private final class LoadModelRenderer extends AExampleRenderer {
		private PointLight mLight;
		private Object3D mObjectGroup;
		private Animation3D mCameraAnim, mLightAnim;

		public LoadModelRenderer(Context context) {
			super(context);
		}

        @Override
		protected void initScene() {
			mLight = new PointLight();
			mLight.setPosition(0, 0, 40);
			mLight.setPower(1000);
			getCurrentScene().addLight(mLight);


//			mLight = new PointLight();
//			mLight.setPosition(0, 400, 0);
//			mLight.setPower(500);
//			getCurrentScene().addLight(mLight);
//
//			mLight = new PointLight();
//			mLight.setPosition(400, 0, 0);
//			mLight.setPower(500);
//			getCurrentScene().addLight(mLight);

			getCurrentCamera().setZ(20);
			getCurrentCamera().setY(-50);
			getCurrentCamera().setCameraPitch(-45);
			getCurrentCamera().setFarPlane(1000);
			LoaderOBJ objParser = new LoaderOBJ(mContext.getResources(),
					mTextureManager, R.raw.mission_obj);
//			LoaderOBJ objParser = new LoaderOBJ(mContext.getResources(),
//					mTextureManager, R.raw.multiobjects_obj);
			try {
				objParser.parse();
				mObjectGroup = objParser.getParsedObject();
//				Material material = new Material();
//				material.enableLighting(true);
//				material.setDiffuseMethod(new DiffuseMethod.Lambert());
//				material.setColor(0x990000);
//
//				mObjectGroup.setMaterial(material);
				getCurrentScene().addChild(mObjectGroup);

				mCameraAnim = new RotateOnAxisAnimation(Vector3.Axis.Y, 360);
				mCameraAnim.setDurationMilliseconds(5000);
				mCameraAnim.setRepeatMode(Animation.RepeatMode.INFINITE);
				mCameraAnim.setTransformable3D(mObjectGroup);
			} catch (ParsingException e) {
				e.printStackTrace();
			}

			mLightAnim = new EllipticalOrbitAnimation3D(new Vector3(),
					new Vector3(0, 10, 0), Vector3.getAxisVector(Vector3.Axis.Z), 0,
					360, EllipticalOrbitAnimation3D.OrbitDirection.CLOCKWISE);

			mLightAnim.setDurationMilliseconds(3000);
			mLightAnim.setRepeatMode(Animation.RepeatMode.INFINITE);
			mLightAnim.setTransformable3D(mLight);

			getCurrentScene().registerAnimation(mCameraAnim);
			getCurrentScene().registerAnimation(mLightAnim);

//			mCameraAnim.play();
//			mLightAnim.play();
		}
		public void setCameraOffset(Vector3 offset) {
			// -- change the camera offset
			Camera currentCamera = getCurrentCamera();
			currentCamera.setX(offset.x);
			currentCamera.setY(offset.y);
			currentCamera.setZ(offset.z);
		}

	}

}
