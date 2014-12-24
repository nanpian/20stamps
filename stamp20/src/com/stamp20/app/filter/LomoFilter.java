/**
 * 
 */ 
package com.stamp20.app.filter; 

import com.stamp20.app.filter.BrightContrastFilter;
import com.stamp20.app.filter.GradientMapFilter;
import com.stamp20.app.filter.IImageFilter;
import com.stamp20.app.util.Image;
import com.stamp20.app.filter.ImageBlender;
import com.stamp20.app.filter.NoiseFilter;
import com.stamp20.app.filter.VignetteFilter;
import com.stamp20.app.filter.ImageBlender.BlendMode;
import android.graphics.Bitmap;

/** 
 * @author 作者 E-mail: 
 * @version 创建时间：2014-12-23 下午5:10:01 
 * 类说明 
 */

public class LomoFilter implements IImageFilter{
	 private BrightContrastFilter contrastFx = new BrightContrastFilter();
    private GradientMapFilter gradientMapFx  = new GradientMapFilter();
    private ImageBlender blender = new ImageBlender();
    private VignetteFilter vignetteFx = new VignetteFilter();
    private NoiseFilter noiseFx = new NoiseFilter();

    public LomoFilter()
    {
        contrastFx.BrightnessFactor = 0.05f;
        contrastFx.ContrastFactor = 0.5f;
     
        blender.Mixture = 0.5f;
        blender.Mode = BlendMode.Multiply;
    
        vignetteFx.Size = 0.6f;

        noiseFx.Intensity = 0.02f;
    }

    public Image process(Image imageIn)
    {
        Image tempImg = contrastFx.process(imageIn);
        tempImg = noiseFx.process(tempImg);
        imageIn = gradientMapFx.process(tempImg);
        imageIn = blender.Blend(imageIn, tempImg);
        imageIn = vignetteFx.process(imageIn);
        return imageIn;
    }

	/* (non-Javadoc)
	 * @see com.stamp20.app.filter.IImageFilter#process(android.graphics.Bitmap)
	 */
	@Override
	public Bitmap process(Bitmap imageIn) {
		// TODO Auto-generated method stub
		Image temp = new Image(imageIn);
	       Image tempImg = contrastFx.process(temp);
	        tempImg = noiseFx.process(tempImg);
	        temp = gradientMapFx.process(tempImg);
	        temp = blender.Blend(temp, tempImg);
	        temp = vignetteFx.process(temp);
	        return temp.getImage();
	}
}

 