package tk.stepcounter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.mozilla.universalchardet.UniversalDetector;

import rocky.common.CommonUtil;
import rocky.common.PropertiesManager;
import rocky.sizecounter.ISizeCounter;
import rocky.sizecounter.SizeMetaData;
import rocky.sizecounter.SourceMetaData;
import rocky.sizecounter.UnsupportedFileType;

public class StepCounterImpl implements ISizeCounter {
	static final Logger LOG = Logger.getLogger("StepCounterImpl");
	static PropertiesManager promanager = null;

	static ArrayList extArray = new ArrayList<String>();

	//Transfer information from ApplicationResources.properties to ArrayList
	static {
		try {
			promanager = new PropertiesManager("/ApplicationResources.properties");
			String supportFile = promanager.getProperty("SupportFiles");
			String[] exts = supportFile.split(" ");

			for (String ext : exts) {
				extArray.add(ext);
			}
		} catch (Exception ex) {
			LOG.error(
					"Load configuration file ApplicationResources.properties",
					ex);
		}

	}
/*
 * The method skip get file which is not countable
 * extFile variable is String. this is Extension(with or without) dot or path 
 * @see rocky.sizecounter.ISizeCounter#isCountable(java.lang.String)
 */
	public boolean isCountable(String extFile) {
		String ext;
		
		if (extFile == null) {
			return false;
		}

		int idxLastDot = extFile.lastIndexOf(".");
		if (idxLastDot > -1) {
			ext = extFile.substring(idxLastDot +1); // get extension without dot
		} else {
			ext = extFile;
		}
		return extArray.contains(ext);
	}

	@Override
	public SourceMetaData countLOC(String filePath, String csName)
			throws UnsupportedFileType {
		SourceMetaData srcMd = new SourceMetaData();
		StepCounter stepCounter = StepCounterFactory.getCounter(filePath);
		CountResult cntRes;

		if (stepCounter != null) {

			try {
				String charset = getCharSetFile(filePath);
				cntRes = stepCounter.count(new File(filePath), charset);
				if (cntRes != null) {
					srcMd.setLoc(cntRes.getStep());
					srcMd.setComment(cntRes.getComment());
				} else {
					LOG.warn("Could not count file extension:"
							+ CommonUtil.getExtension(filePath));
				}

				return srcMd;

			} catch (IOException ioex) {
				LOG.error("Error in counting size of file '" + filePath
						+ "' csName=" + csName, ioex);
			}
		} else {
			LOG.warn("Don't support counter for file extension:"
					+ CommonUtil.getExtension(filePath));
			throw new UnsupportedFileType(
					"Don't support counter for file extension:"
							+ CommonUtil.getExtension(filePath));
		}

		return null;
	}

	@Override
	public SourceMetaData countLOC(String filePath) throws UnsupportedFileType {
		SourceMetaData srcMd = new SourceMetaData();
		StepCounter stepCounter = StepCounterFactory.getCounter(filePath);
		CountResult cntRes;

		if (stepCounter != null) {

			try {
				String charset = getCharSetFile(filePath);
				cntRes = stepCounter.count(new File(filePath), charset);

				if (cntRes != null) {
					srcMd.setLoc(cntRes.getStep());
					srcMd.setComment(cntRes.getComment());
				} else {
					LOG.warn("Could not count file extension:"
							+ CommonUtil.getExtension(filePath));
				}

				return srcMd;

			} catch (IOException ioex) {
				LOG.error("Error in counting size of file '" + filePath + "'",
						ioex);
			}
		} else {
			LOG.warn("Don't support counter for file extension:"
					+ CommonUtil.getExtension(filePath));
			throw new UnsupportedFileType(
					"Don't support counter for file extension:"
							+ CommonUtil.getExtension(filePath));
		}

		return null;
	}

	/**
	 * find a encoding of file.By read a file to end determine encoding by
	 * content a file if not find out a encoding a file, default is ANSI
	 * 
	 * @param filePath
	 * @return
	 */
	public String getCharSetFile(String filePath) {
		// default charset for a file, if can't detect encoding a file, it means
		// encoding of file is 'ANSI'
		String defaultCharset = "UTF-8";

		String fileCharset = null;
		byte[] buf = new byte[4096];
		try {
			java.io.FileInputStream fis = new java.io.FileInputStream(filePath);

			// (1)
			UniversalDetector detector = new UniversalDetector(null);

			// (2)
			int nread;
			while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
				detector.handleData(buf, 0, nread);
			}
			// (3)
			detector.dataEnd();

			// (4)
			fileCharset = detector.getDetectedCharset();
			if (fileCharset == null) {
				fileCharset = defaultCharset;
			}

			// (5)
			detector.reset();
			fis.close();
		} catch (IOException ioe) {
			LOG.error("Can't find out file at Path: '" + filePath + "'");
			fileCharset = null;
		}
		return fileCharset;
	}

    /* 
     * Explain the description for this method here
     * @see rocky.sizecounter.ISizeCounter#countSize(java.lang.String)
     */
    @Override
    public SizeMetaData countSize(String filePath) throws UnsupportedFileType {
        // TODO Auto-generated method stub
        return null;
    }
}
