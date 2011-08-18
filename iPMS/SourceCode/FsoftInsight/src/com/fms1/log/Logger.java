/*
 * Copyright (c) 2009, FPT Software JSC
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *	- Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *	- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *	- Neither the name of the FPT Software JSC nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, 
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, 
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, 
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
 
 package com.fms1.log;

import org.apache.log4j.Level;

/**
 * @date   Created on Sep 30, 2005
 *  
 */
public class Logger {
	/** Full-qualified package name and its abbreviation. */
	private static final String FULL_NAME = "com.fms1";
	private static final String SHORT_NAME = "FSI";

	/** Name of Logger to log statistics information. */
	private static final String STATS_NAME = "STATISTICS";

	/** Custom level for statistics loggings. */
	private static final String STATS_LEVEL = "STATS";

	/** Constants for statistics loggings. */
	private static final String STATS_BEGIN = "EjbBegin";
	private static final String STATS_ENDOK = "EjbEndOK";
	private static final String STATS_DELIM = "|";

	/** Flag to distinguish statistics Logger instance. */
	private boolean m_IsStats = false;

	/** Reference to the Log4J instance being wrapped. */
	private org.apache.log4j.Logger m_Log = null;

	/**
	 * Initialize Log4J with specified property file.
	 * Called when LSBServlet is initialized.
	 *
	 * @param propsUrl the URL of LoginLog4J property file
	 */
	public static void initLogger(String propsUrl) {
		org.apache.log4j.PropertyConfigurator.configure(propsUrl);
	}

	/**
	 * Create an instance for normal loggings with specified Log4J instance.
	 *
	 * @param log4j the instance of Log4J Logger to wrap
	 */
	private Logger(org.apache.log4j.Logger log4j) {
		this(log4j, false);
	}

	/**
	 * Create an instance for normal or statistics loggings
	 * with specified Log4J instance.
	 *
	 * @param log4j the instance of Log4J Logger to wrap
	 * @param isStats true if for statistics loggings, false otherwise
	 */
	private Logger(org.apache.log4j.Logger log4j, boolean isStats) {
		m_Log = log4j;
		m_IsStats = isStats;
	}

	/**
	 * Get an instance of Logger for specified class.
	 *
	 * @param clazz the class that requires an instance of Logger
	 * @return an instance of Logger for specified class
	 */
	public static Logger getLogger(Class clazz) {
		String name = clazz.getName();
		if (name.startsWith(FULL_NAME)) {
			name = SHORT_NAME + name.substring(FULL_NAME.length());
		}
		return getLogger(name);
	}

	/**
	 * Get an instance of Logger for specified name.
	 *
	 * @param name the name to get Log4J Logger instance for
	 * @return an instance of Logger for specified name
	 */
	public static Logger getLogger(String name) {
		return new Logger(org.apache.log4j.Logger.getLogger(name));
	}

	/**
	 * Log a message at DEBUG level.
	 *
	 * <p><b>WARNING</b> Note that passing a {@link java.lang.Throwable} to this
	 * method will print the name of the Throwable but no stack trace. To
	 * print a stack trace use the {@link #debug(java.lang.Object, java.lang.Throwable)} form
	 * instead.
	 *
	 * @param message message object to be logged
	 */
	public void debug(Object message) {
		m_Log.debug(message);
	}

	/**
	 * Log a message with stack trace of Throwable at DEBUG level.
	 *
	 * @param message message object to be logged
	 * @param t a Throwable to log stact trace
	 */
	public void debug(Object message, Throwable t) {
		m_Log.debug(message, t);
	}

	/**
	 * Log a message at ERROR level.
	 *
	 * <p><b>WARNING</b> Note that passing a {@link java.lang.Throwable} to this
	 * method will print the name of the Throwable but no stack trace. To
	 * print a stack trace use the {@link #error(java.lang.Object, java.lang.Throwable)} form
	 * instead.
	 *
	 * @param message message object to be logged
	 */
	public void error(Object message) {
		m_Log.error(message);
	}

	/**
	 * Log a message with stack trace of Throwable at ERROR level.
	 *
	 * @param message message object to be logged
	 * @param t a Throwable to log stact trace
	 */
	public void error(Object message, Throwable t) {
		m_Log.error(message, t);
	}

	/**
	 * Log a message at FATAL level.
	 *
	 * <p><b>WARNING</b> Note that passing a {@link java.lang.Throwable} to this
	 * method will print the name of the Throwable but no stack trace. To
	 * print a stack trace use the {@link #fatal(java.lang.Object, java.lang.Throwable)} form
	 * instead.
	 *
	 * @param message message object to be logged
	 */
	public void fatal(Object message) {
		m_Log.fatal(message);
	}

	/**
	 * Log a message with stack trace of Throwable at FATAL level.
	 *
	 * @param message message object to be logged
	 * @param t a Throwable to log stact trace
	 */
	public void fatal(Object message, Throwable t) {
		m_Log.fatal(message, t);
	}

	/**
	 * Log a message at INFO level.
	 *
	 * <p><b>WARNING</b> Note that passing a {@link java.lang.Throwable} to this
	 * method will print the name of the Throwable but no stack trace. To
	 * print a stack trace use the {@link #info(java.lang.Object, java.lang.Throwable)} form
	 * instead.
	 *
	 * @param message message object to be logged
	 */
	public void info(Object message) {
		m_Log.info(message);
	}

	/**
	 * Log a message with stack trace of Throwable at INFO level.
	 *
	 * @param message message object to be logged
	 * @param t a Throwable to log stact trace
	 */
	public void info(Object message, Throwable t) {
		m_Log.info(message, t);
	}

	/**
	 * Log a message at WARN level.
	 *
	 * <p><b>WARNING</b> Note that passing a {@link java.lang.Throwable} to this
	 * method will print the name of the Throwable but no stack trace. To
	 * print a stack trace use the {@link #warn(java.lang.Object, java.lang.Throwable)} form
	 * instead.
	 *
	 * @param message message object to be logged
	 */
	public void warn(Object message) {
		m_Log.warn(message);
	}

	/**
	 * Log a message with stack trace of Throwable at WARN level.
	 *
	 * @param message message object to be logged
	 * @param t a Throwable to log stact trace
	 */
	public void warn(Object message, Throwable t) {
		m_Log.warn(message, t);
	}

	/**
	 * Get an instance of Logger for statistics features.<P>
	 * Used in EJB classes to log statistics information.
	 *
	 * @return an instance of Logger for statistics features
	 */
	public static Logger getStatisticsLogger() {
		return new Logger(org.apache.log4j.Logger.getLogger(STATS_NAME), true);
	}

	/**
	 * Log a statistic information at beginning of execution of an EJB service.
	 *
	 * @param serviceName name of the EJB service in execution
	 * @param freeMsg any free message relates to the EJB service
	 */
	public void statsBegin(String serviceName, String freeMsg) {
		if (!m_IsStats)
			return;
		this.stats(
			STATS_BEGIN + STATS_DELIM + serviceName + STATS_DELIM + freeMsg);
	}

	/**
	 * Log a statistic information after an EJB service has been processed
	 * successfully.
	 *
	 * @param serviceName name of the EJB service in execution
	 * @param time the milliseconds spent for this service execution
	 * @param freeMsg any free message relates to the EJB service
	 */
	public void statsEndOK(String serviceName, long time, String freeMsg) {
		if (!m_IsStats)
			return;
		this.stats(
				STATS_ENDOK
				+ STATS_DELIM
				+ serviceName
				+ STATS_DELIM
				+ time
				+ STATS_DELIM
				+ freeMsg);
	}

	/**
	 * Log a statistic information to log file.
	 *
	 * @param msg  the statistic message to log
	 */
	private void stats(String msg) {
		m_Log.log(StatsLogLevel.STATS, msg);
	}

	/**
	 * Define a new log level (STATS) for statistic messages.<P>
	 * This level has higher priority than FATAL so that it can't be
	 * disabled in logger property file.
	 */
	public static class StatsLogLevel extends Level {
		public static final int STATS_INT = Level.FATAL_INT + 1;
		public static final String STATS_STR = STATS_LEVEL;

		public static final StatsLogLevel STATS =
			new StatsLogLevel(STATS_INT, STATS_STR, 0);

		protected StatsLogLevel(int level, String strLevel, int syslogEquiv) {
			super(level, strLevel, syslogEquiv);
		}

		/**
		 * Convert the string passed as argument to a level. If the
		 * conversion fails, then this method returns {@link #STATS}.
		 */
		public static Level toLevel(String sArg) {
			return toLevel(sArg, StatsLogLevel.STATS);
		}

		public static Level toLevel(String sArg, Level defaultValue) {
			if (sArg == null) {
				return defaultValue;
			} else if (STATS_STR.equals(sArg.toUpperCase())) {
				return StatsLogLevel.STATS;
			} else {
				return Level.toLevel(sArg, defaultValue);
			}
		}

		public static Level toLevel(int i) throws IllegalArgumentException {
			switch (i) {
				case STATS_INT:
					return StatsLogLevel.STATS;
				default :
					return Level.toLevel(i);
			}
		}
	}
}
