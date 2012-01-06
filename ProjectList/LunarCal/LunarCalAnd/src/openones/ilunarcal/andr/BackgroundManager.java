package openones.ilunarcal.andr;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class BackgroundManager {
	private static List<Bitmap> bitmap;
	private static List<String> urls;
	public static boolean haveImage = false;
	public static boolean extendStorage = true;

	public BackgroundManager() {
		// TODO Auto-generated constructor stub
		/*
		 * if(!createFolderImages()){ return; }
		 */
		createFolderImages();
		urls = new ArrayList<String>();
		urls = ReadSDCard();
		if (!urls.isEmpty())
			haveImage = true;
		bitmap = new ArrayList<Bitmap>();
		bitmap = GetBitmapFrmUrlLst(urls);
	}

	private static List<String> ReadSDCard() {
		List<String> tFileList = new ArrayList<String>();
		File f;
		// It have to be matched with the directory in SDCard
		if (extendStorage) {
			f = new File(Environment.getExternalStorageDirectory()
					+ "/LichAmDuong/images");
		} else {
			f = new File(Environment.getRootDirectory()
					+ "/LichAmDuong/images");
		}
		File[] files = f.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String filename) {
				// TODO Auto-generated method stub
				return ((filename.endsWith(".jpg")) || (filename
						.endsWith(".png")));
			}
		});

		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			/*
			 * It's assumed that all file in the path are in supported type
			 */
			tFileList.add(file.getPath());
		}

		return tFileList;
	}

	private static List<Bitmap> GetBitmapFrmUrlLst(List<String> urlList) {
		List<Bitmap> list = new ArrayList<Bitmap>();
		for (int i = 0; i < urlList.size(); i++) {
			list.add(BitmapFactory.decodeFile(urlList.get(i)));
		}
		return list;
	}

	private static boolean createFolderImages() {
		try {
			File dir = new File(Environment.getExternalStorageDirectory()
					+ "/LichAmDuong/images");
			extendStorage = true;
			return (dir.mkdir());
		} catch (Exception e) {
			// TODO: handle exception
			File dir = new File(Environment.getRootDirectory()
					+ "/LichAmDuong/images");
			extendStorage = false;
			return (dir.mkdir());
		}

	}

	public static Bitmap getRadomBitmap() {
		int randomlength = bitmap.size();
		int randomNum = Calendar.getInstance().get(Calendar.MILLISECOND);
		if (randomlength == 0)
			return null;
		return bitmap.get(randomNum % randomlength);
	}
}
