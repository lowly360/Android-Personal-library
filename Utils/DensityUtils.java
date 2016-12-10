

import android.content.Context;
import android.view.WindowManager;

/**
 * int 转换为对应dpi的dp工具类
 *
 * @author itlowly
 */
public class DensityUtils {
	private static DensityUtils utils;
	private Context context;
	
	private DensityUtils{
		
	}
	
	public DensityUtils getInstance(){
		if(utils == null){
			return new DensityUtils();
		}else{
			return utils;
		}
	}
	
	/**
	 * 必须初始化上下文
	 * 初始化工具类上下文
	*/
	public void initUtils(Context context){
		
		this.context = context;
		
	}

	/**
	 * dp 转 px
	 *
	 * @param context
	 * @param dp
	 * @return
	 */
	public int dp2px(float dp) {
		float density = context.getResources().getDisplayMetrics().density;
		//System.out.println("dpi = "+density);
		int px = (int) (dp * density + 0.5f); //四舍五入
		return px;
	}

	/**
	 * px 转 dp
	 *
	 * @param context
	 * @param px
	 * @return
	 */
	public float px2dp(int px) {
		float density = context.getResources().getDisplayMetrics().density;
		//System.out.println("dpi = "+density);
		return px / density;
	}

	/**
	 * 获取屏幕宽度
	 *
	 * @param context
	 * @return
	 */
	public float getMaxWigthDp() {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();

		return px2dp(context, width);
	}

	/**
	 * 获取屏幕高度
	 *
	 * @param context
	 * @return
	 */
	public float getMaxHeightDp() {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		return px2dp(context, wm.getDefaultDisplay().getHeight());
	}

	public float getMaxWigthPx() {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();

		return width;
	}

	public float getMaxHeightPx() {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getHeight();
	}
}
