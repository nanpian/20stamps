package com.stamp20.app.adapter;

import java.util.ArrayList;
import java.util.List;

import com.stamp20.filter.AutoAdjustFilter;
import com.stamp20.filter.BannerFilter;
import com.stamp20.filter.BigBrotherFilter;
import com.stamp20.filter.BlackWhiteFilter;
import com.stamp20.filter.BlindFilter;
import com.stamp20.filter.BlockPrintFilter;
import com.stamp20.filter.BrickFilter;
import com.stamp20.filter.BrightContrastFilter;
import com.stamp20.filter.CleanGlassFilter;
import com.stamp20.filter.ColorQuantizeFilter;
import com.stamp20.filter.ColorToneFilter;
import com.stamp20.filter.ComicFilter;
import com.stamp20.filter.EdgeFilter;
import com.stamp20.filter.FeatherFilter;
import com.stamp20.filter.FillPatternFilter;
import com.stamp20.filter.FilmFilter;
import com.stamp20.filter.FocusFilter;
import com.stamp20.filter.GammaFilter;
import com.stamp20.filter.GaussianBlurFilter;
import com.stamp20.filter.Gradient;
import com.stamp20.filter.HslModifyFilter;
import com.stamp20.filter.IImageFilter;
import com.stamp20.filter.IllusionFilter;
import com.stamp20.filter.InvertFilter;
import com.stamp20.filter.LensFlareFilter;
import com.stamp20.filter.LightFilter;
import com.stamp20.filter.LomoFilter;
import com.stamp20.filter.MirrorFilter;
import com.stamp20.filter.MistFilter;
import com.stamp20.filter.MonitorFilter;
import com.stamp20.filter.MosaicFilter;
import com.stamp20.filter.NeonFilter;
import com.stamp20.filter.NightVisionFilter;
import com.stamp20.filter.NoiseFilter;
import com.stamp20.filter.OilPaintFilter;
import com.stamp20.filter.OldPhotoFilter;
import com.stamp20.filter.PaintBorderFilter;
import com.stamp20.filter.PixelateFilter;
import com.stamp20.filter.PosterizeFilter;
import com.stamp20.app.R;
import com.stamp20.filter.RadialDistortionFilter;
import com.stamp20.filter.RainBowFilter;
import com.stamp20.filter.RaiseFrameFilter;
import com.stamp20.filter.RectMatrixFilter;
import com.stamp20.filter.ReflectionFilter;
import com.stamp20.filter.ReliefFilter;
import com.stamp20.filter.SaturationModifyFilter;
import com.stamp20.filter.SceneFilter;
import com.stamp20.filter.SepiaFilter;
import com.stamp20.filter.SharpFilter;
import com.stamp20.filter.ShiftFilter;
import com.stamp20.filter.SmashColorFilter;
import com.stamp20.filter.SoftGlowFilter;
import com.stamp20.filter.SupernovaFilter;
import com.stamp20.filter.ThreeDGridFilter;
import com.stamp20.filter.ThresholdFilter;
import com.stamp20.filter.TileReflectionFilter;
import com.stamp20.filter.TintFilter;
import com.stamp20.filter.VideoFilter;
import com.stamp20.filter.VignetteFilter;
import com.stamp20.filter.VintageFilter;
import com.stamp20.filter.WaterWaveFilter;
import com.stamp20.filter.XRadiationFilter;
import com.stamp20.filter.YCBCrLinearFilter;
import com.stamp20.filter.ZoomBlurFilter;
import com.stamp20.filter.Distort.BulgeFilter;
import com.stamp20.filter.Distort.RippleFilter;
import com.stamp20.filter.Distort.TwistFilter;
import com.stamp20.filter.Distort.WaveFilter;
import com.stamp20.app.textures.CloudsTexture;
import com.stamp20.app.textures.LabyrinthTexture;
import com.stamp20.app.textures.MarbleTexture;
import com.stamp20.app.textures.TextileTexture;
import com.stamp20.app.textures.TexturerFilter;
import com.stamp20.app.textures.WoodTexture;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import com.stamp20.app.R;

public class ImageFilterAdapter extends BaseAdapter {
	private class FilterInfo {
		public int filterID;
		public IImageFilter filter;

		public FilterInfo(int filterID, IImageFilter filter) {
			this.filterID = filterID;
			this.filter = filter;
		}
	}

	private Context mContext;
	private List<FilterInfo> filterArray = new ArrayList<FilterInfo>();

	public ImageFilterAdapter(Context c) {
		mContext = c;
		
		//99��Ч��
         
        //v0.4 
		filterArray.add(new FilterInfo(R.drawable.video_filter1, new VideoFilter(VideoFilter.VIDEO_TYPE.VIDEO_STAGGERED)));
		filterArray.add(new FilterInfo(R.drawable.video_filter2, new VideoFilter(VideoFilter.VIDEO_TYPE.VIDEO_TRIPED)));
		filterArray.add(new FilterInfo(R.drawable.video_filter3, new VideoFilter(VideoFilter.VIDEO_TYPE.VIDEO_3X3)));
		filterArray.add(new FilterInfo(R.drawable.video_filter4, new VideoFilter(VideoFilter.VIDEO_TYPE.VIDEO_DOTS)));
		filterArray.add(new FilterInfo(R.drawable.tilereflection_filter1, new TileReflectionFilter(20, 8, 45, (byte)1)));
		filterArray.add(new FilterInfo(R.drawable.tilereflection_filter2, new TileReflectionFilter(20, 8, 45, (byte)2)));
		filterArray.add(new FilterInfo(R.drawable.fillpattern_filter, new FillPatternFilter((Activity) mContext, R.drawable.texture1)));
		filterArray.add(new FilterInfo(R.drawable.fillpattern_filter1, new FillPatternFilter((Activity) mContext, R.drawable.texture2)));
		filterArray.add(new FilterInfo(R.drawable.mirror_filter1, new MirrorFilter(true)));
		filterArray.add(new FilterInfo(R.drawable.mirror_filter2, new MirrorFilter(false)));
		filterArray.add(new FilterInfo(R.drawable.ycb_crlinear_filter, new YCBCrLinearFilter(new YCBCrLinearFilter.Range(-0.3f, 0.3f))));
		filterArray.add(new FilterInfo(R.drawable.ycb_crlinear_filter2, new YCBCrLinearFilter(new YCBCrLinearFilter.Range(-0.276f, 0.163f), new YCBCrLinearFilter.Range(-0.202f, 0.5f))));
		filterArray.add(new FilterInfo(R.drawable.texturer_filter, new com.stamp20.app.textures.TexturerFilter(new com.stamp20.app.textures.CloudsTexture(), 0.8f, 0.8f)));
		filterArray.add(new FilterInfo(R.drawable.texturer_filter1, new com.stamp20.app.textures.TexturerFilter(new com.stamp20.app.textures.LabyrinthTexture(), 0.8f, 0.8f)));
		filterArray.add(new FilterInfo(R.drawable.texturer_filter2, new com.stamp20.app.textures.TexturerFilter(new com.stamp20.app.textures.MarbleTexture(), 1.8f, 0.8f)));
		filterArray.add(new FilterInfo(R.drawable.texturer_filter3, new com.stamp20.app.textures.TexturerFilter(new com.stamp20.app.textures.WoodTexture(), 0.8f, 0.8f)));
		filterArray.add(new FilterInfo(R.drawable.texturer_filter4, new com.stamp20.app.textures.TexturerFilter(new com.stamp20.app.textures.TextileTexture(), 0.8f, 0.8f)));
		filterArray.add(new FilterInfo(R.drawable.hslmodify_filter, new HslModifyFilter(20f)));
		filterArray.add(new FilterInfo(R.drawable.hslmodify_filter0, new HslModifyFilter(40f)));
		filterArray.add(new FilterInfo(R.drawable.hslmodify_filter1, new HslModifyFilter(60f)));
		filterArray.add(new FilterInfo(R.drawable.hslmodify_filter2, new HslModifyFilter(80f)));
		filterArray.add(new FilterInfo(R.drawable.hslmodify_filter3, new HslModifyFilter(100f)));
		filterArray.add(new FilterInfo(R.drawable.hslmodify_filter4, new HslModifyFilter(150f)));
		filterArray.add(new FilterInfo(R.drawable.hslmodify_filter5, new HslModifyFilter(200f)));
		filterArray.add(new FilterInfo(R.drawable.hslmodify_filter6, new HslModifyFilter(250f)));
		filterArray.add(new FilterInfo(R.drawable.hslmodify_filter7, new HslModifyFilter(300f)));
		
		//v0.3  
		filterArray.add(new FilterInfo(R.drawable.zoomblur_filter, new ZoomBlurFilter(30)));
		filterArray.add(new FilterInfo(R.drawable.threedgrid_filter, new ThreeDGridFilter(16, 100)));
		filterArray.add(new FilterInfo(R.drawable.colortone_filter, new ColorToneFilter(Color.rgb(33, 168, 254), 192)));
		filterArray.add(new FilterInfo(R.drawable.colortone_filter2, new ColorToneFilter(0x00FF00, 192)));//green
		filterArray.add(new FilterInfo(R.drawable.colortone_filter3, new ColorToneFilter(0xFF0000, 192)));//blue
		filterArray.add(new FilterInfo(R.drawable.colortone_filter4, new ColorToneFilter(0x00FFFF, 192)));//yellow
		filterArray.add(new FilterInfo(R.drawable.softglow_filter, new SoftGlowFilter(10, 0.1f, 0.1f)));
		filterArray.add(new FilterInfo(R.drawable.tilereflection_filter, new TileReflectionFilter(20, 8)));
		filterArray.add(new FilterInfo(R.drawable.blind_filter1, new BlindFilter(true, 96, 100, 0xffffff)));
		filterArray.add(new FilterInfo(R.drawable.blind_filter2, new BlindFilter(false, 96, 100, 0x000000)));
		filterArray.add(new FilterInfo(R.drawable.raiseframe_filter, new RaiseFrameFilter(20)));
		filterArray.add(new FilterInfo(R.drawable.shift_filter, new ShiftFilter(10)));
		filterArray.add(new FilterInfo(R.drawable.wave_filter, new WaveFilter(25, 10)));
		filterArray.add(new FilterInfo(R.drawable.bulge_filter, new BulgeFilter(-97)));
		filterArray.add(new FilterInfo(R.drawable.twist_filter, new TwistFilter(27, 106)));
		filterArray.add(new FilterInfo(R.drawable.ripple_filter, new RippleFilter(38, 15, true)));
		filterArray.add(new FilterInfo(R.drawable.illusion_filter, new IllusionFilter(3)));
		filterArray.add(new FilterInfo(R.drawable.supernova_filter, new SupernovaFilter(0x00FFFF,20,100)));
		filterArray.add(new FilterInfo(R.drawable.lensflare_filter, new LensFlareFilter()));
		filterArray.add(new FilterInfo(R.drawable.posterize_filter, new PosterizeFilter(2)));
		filterArray.add(new FilterInfo(R.drawable.gamma_filter, new GammaFilter(50)));
		filterArray.add(new FilterInfo(R.drawable.sharp_filter, new SharpFilter()));
		
		//v0.2
		filterArray.add(new FilterInfo(R.drawable.invert_filter, new ComicFilter()));
		filterArray.add(new FilterInfo(R.drawable.invert_filter, new SceneFilter(5f, Gradient.Scene())));//green
		filterArray.add(new FilterInfo(R.drawable.invert_filter, new SceneFilter(5f, Gradient.Scene1())));//purple
		filterArray.add(new FilterInfo(R.drawable.invert_filter, new SceneFilter(5f, Gradient.Scene2())));//blue
		filterArray.add(new FilterInfo(R.drawable.invert_filter, new SceneFilter(5f, Gradient.Scene3())));
		filterArray.add(new FilterInfo(R.drawable.invert_filter, new FilmFilter(80f)));
		filterArray.add(new FilterInfo(R.drawable.invert_filter, new FocusFilter()));
		filterArray.add(new FilterInfo(R.drawable.invert_filter, new CleanGlassFilter()));
		filterArray.add(new FilterInfo(R.drawable.invert_filter, new PaintBorderFilter(0x00FF00)));//green
		filterArray.add(new FilterInfo(R.drawable.invert_filter, new PaintBorderFilter(0x00FFFF)));//yellow
		filterArray.add(new FilterInfo(R.drawable.invert_filter, new PaintBorderFilter(0xFF0000)));//blue
		filterArray.add(new FilterInfo(R.drawable.invert_filter, new LomoFilter()));
		
		//v0.1
		filterArray.add(new FilterInfo(R.drawable.invert_filter, new InvertFilter()));
		filterArray.add(new FilterInfo(R.drawable.blackwhite_filter, new BlackWhiteFilter()));
		filterArray.add(new FilterInfo(R.drawable.edge_filter, new EdgeFilter()));
		filterArray.add(new FilterInfo(R.drawable.pixelate_filter, new PixelateFilter()));
		filterArray.add(new FilterInfo(R.drawable.neon_filter, new NeonFilter()));
		filterArray.add(new FilterInfo(R.drawable.bigbrother_filter, new BigBrotherFilter()));
		filterArray.add(new FilterInfo(R.drawable.monitor_filter, new MonitorFilter()));
		filterArray.add(new FilterInfo(R.drawable.relief_filter, new ReliefFilter()));
		filterArray.add(new FilterInfo(R.drawable.brightcontrast_filter,new BrightContrastFilter()));
		filterArray.add(new FilterInfo(R.drawable.saturationmodity_filter,	new SaturationModifyFilter()));
		filterArray.add(new FilterInfo(R.drawable.threshold_filter,	new ThresholdFilter()));
		filterArray.add(new FilterInfo(R.drawable.noisefilter,	new NoiseFilter()));
		filterArray.add(new FilterInfo(R.drawable.banner_filter1, new BannerFilter(10, true)));
		filterArray.add(new FilterInfo(R.drawable.banner_filter2, new BannerFilter(10, false)));
		filterArray.add(new FilterInfo(R.drawable.rectmatrix_filter, new RectMatrixFilter()));
		filterArray.add(new FilterInfo(R.drawable.blockprint_filter, new BlockPrintFilter()));
		filterArray.add(new FilterInfo(R.drawable.brick_filter,	new BrickFilter()));
		filterArray.add(new FilterInfo(R.drawable.gaussianblur_filter,	new GaussianBlurFilter()));
		filterArray.add(new FilterInfo(R.drawable.light_filter,	new LightFilter()));
		filterArray.add(new FilterInfo(R.drawable.mosaic_filter,new MistFilter()));
		filterArray.add(new FilterInfo(R.drawable.mosaic_filter,new MosaicFilter()));
		filterArray.add(new FilterInfo(R.drawable.oilpaint_filter,	new OilPaintFilter()));
		filterArray.add(new FilterInfo(R.drawable.radialdistortion_filter,new RadialDistortionFilter()));
		filterArray.add(new FilterInfo(R.drawable.reflection1_filter,new ReflectionFilter(true)));
		filterArray.add(new FilterInfo(R.drawable.reflection2_filter,new ReflectionFilter(false)));
		filterArray.add(new FilterInfo(R.drawable.saturationmodify_filter,	new SaturationModifyFilter()));
		filterArray.add(new FilterInfo(R.drawable.smashcolor_filter,new SmashColorFilter()));
		filterArray.add(new FilterInfo(R.drawable.tint_filter,	new TintFilter()));
		filterArray.add(new FilterInfo(R.drawable.vignette_filter,	new VignetteFilter()));
		filterArray.add(new FilterInfo(R.drawable.autoadjust_filter,new AutoAdjustFilter()));
		filterArray.add(new FilterInfo(R.drawable.colorquantize_filter,	new ColorQuantizeFilter()));
		filterArray.add(new FilterInfo(R.drawable.waterwave_filter,	new WaterWaveFilter()));
		filterArray.add(new FilterInfo(R.drawable.vintage_filter,new VintageFilter()));
		filterArray.add(new FilterInfo(R.drawable.oldphoto_filter,new OldPhotoFilter()));
		filterArray.add(new FilterInfo(R.drawable.sepia_filter,	new SepiaFilter()));
		filterArray.add(new FilterInfo(R.drawable.rainbow_filter,new RainBowFilter()));
		filterArray.add(new FilterInfo(R.drawable.feather_filter,new FeatherFilter()));
		filterArray.add(new FilterInfo(R.drawable.xradiation_filter,new XRadiationFilter()));
		filterArray.add(new FilterInfo(R.drawable.nightvision_filter,new NightVisionFilter()));

		filterArray.add(new FilterInfo(R.drawable.saturationmodity_filter,null/* �˴������ԭͼЧ�� */));
	}

	public int getCount() {
		return filterArray.size();
	}

	public Object getItem(int position) {
		return position < filterArray.size() ? filterArray.get(position).filter
				: null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Bitmap bmImg = BitmapFactory
				.decodeResource(mContext.getResources(),
						filterArray.get(position).filterID);
		int width = 100;// bmImg.getWidth();
		int height = 100;// bmImg.getHeight();
		bmImg.recycle();
		ImageView imageview = new ImageView(mContext);
		imageview.setImageResource(filterArray.get(position).filterID);
		imageview.setLayoutParams(new Gallery.LayoutParams(width, height));
		imageview.setScaleType(ImageView.ScaleType.FIT_CENTER);// ������ʾ��������
		return imageview;
	}
};
