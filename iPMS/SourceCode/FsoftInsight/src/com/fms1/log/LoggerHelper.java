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

import com.fms1.exception.FSIException;
/**
 * @date   Created on Sep 30, 2005
 *  
 */
public class LoggerHelper {
	/**
	 * The Logger instance
	 */
	private Logger logger = null;

	private LoggerHelper() {
	}

	/**
	* Constructor for LogHelper
	* Get an instance of Logger for specified class.
	*
	* @param clazz the class that requires an instance of Logger
	*/
	public LoggerHelper(Class clazz) {
		this.logger = Logger.getLogger(clazz);
	}
   
	/**
	* Log a message with stack trace of Exception at ERROR level.
	*
	* @param exception a Exception to log stack trace
	*/
	public void logError(Throwable exception) {
		if (exception instanceof FSIException) {
			FSIException ex = (FSIException) exception;
			if (ex.getThrowable() != null) {
				logger.error(ex.getLocation(), ex.getThrowable());
			} else {
				logger.error(ex.getLocationMessage());
			}
		} else {
			logger.error(exception);
		}
	}
   
	/**
	* Log a message at ERROR level.
	*
	* @param description message object to be logged
	*/
	public void logError(String description) {
		logger.error(description);
	}
   
	/**
	* Log a message with stack trace of Exception at DEBUG level.
	*
	* @param exception a Exception to log stack trace
	*/
	public void logDebug(Throwable exception) {
		if (exception instanceof FSIException) {
			FSIException ex = (FSIException) exception;
			if (ex.getThrowable() != null) {
				logger.debug(ex.getLocation(), ex.getThrowable());
			} else {
				logger.debug(ex.getLocationMessage());
			}
		} else {
			logger.debug(exception);
		}
	}
   
	/**
	* Log a message at DEBUG level.
	*
	* @param description message object to be logged
	*/
	public void logDebug(String description) {
		logger.debug(description);
	}

	/**
	 * Log a message with stack trace of Exception at INFO level.
	 *
	 * @param exception a Exception to log stack trace
	 */
	public void logInfo(Throwable exception) {
		if (exception instanceof FSIException) {
			FSIException ex = (FSIException) exception;
			if (ex.getThrowable() != null) {
				logger.info(ex.getLocation(), ex.getThrowable());
			} else {
				logger.info(ex.getLocationMessage());
			}
		} else {
			logger.info(exception);
		}
	}

	/**
	 * Log a message at INFO level.
	 *
	 * @param description message object to be logged
	 */
	public void logInfo(String description) {
		logger.info(description);
	}
   
	/**
	 * Log a message with stack trace of Exception at WARNING level.
	 *
	 * @param exception a Exception to log stack trace
	 */
	public void logWarn(Throwable exception) {
		if (exception instanceof FSIException) {
			FSIException ex = (FSIException) exception;
			if (ex.getThrowable() != null) {
				logger.warn(ex.getLocation(), ex.getThrowable());
			} else {
				logger.warn(ex.getLocationMessage());
			}
		} else {
			logger.warn(exception);
		}
	}

	/**
	 * Log a message at WARNING level.
	 *
	 * @param description message object to be logged
	 */
	public void logWarn(String description) {
		logger.warn(description);
	}
   
	/**
	 * Log a message with stack trace of Exception at FATAL level.
	 *
	 * @param exception a Exception to log stack trace
	 */
	public void logFatal(Throwable exception) {
		if (exception instanceof FSIException) {
			FSIException ex = (FSIException) exception;
			if (ex.getThrowable() != null) {
				logger.fatal(ex.getLocation(), ex.getThrowable());
			} else {
				logger.fatal(ex.getLocationMessage());
			}
		} else {
			logger.fatal(exception);
		}
	}

	/**
	 * Log a message at FATAL level.
	 *
	 * @param description message object to be logged
	 */
	public void logFatal(String description) {
		logger.fatal(description);
	}
}
