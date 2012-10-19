/*
	This file is part of GooglePlaces.

    GooglePlaces is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    GooglePlaces is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with GooglePlaces.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jjm.android.googleplaces.util;

import java.util.concurrent.Executor;

import android.os.AsyncTask;

/**
 * A nicer version of AsyncTask (which is built on top of AsyncTask).
 * TODO Need more documentation on this class.
 */
public abstract class BackgroundTask<Result> {
	private Exception mException;
	private Result mResult;
	private boolean mCancelled;

	private final AsyncTask<Void, Void, Void> mTask = new AsyncTask<Void, Void, Void>() {
		protected Void doInBackground(Void... params) {
			try {
				mResult = call();
			} catch (Exception ex) {
				mException = ex;
			}
			return null;
		}

		protected void onPreExecute() {
			onBefore();
		}

		protected void onPostExecute(Void result) {
			try {
				if (mException == null) { 
					if (mCancelled) {
						BackgroundTask.this.onCancelled();
					} else {
						onSuccess(mResult);
					}
				}else{
					onException(mException);
				}
			} finally {
				onFinally();
			}
		}
	};

	/**
	 * @see AsyncTask#execute(Object...)
	 */
	public void execute(){
		mTask.execute();
	}
	
	/**
	 * @see AsyncTask#executeOnExecutor(Executor, Object...) 
	 */
	public void executeOnExecutor(Executor executor){
		mTask.executeOnExecutor(executor);
	}
	
	/**
	 * @see AsyncTask#cancel(boolean) 
	 */
	public void cancel(boolean mayInterruptIfRunning) {
		mCancelled = true;
		mTask.cancel(mayInterruptIfRunning);
	}

	/**
	 * Called on the UI thread before the task executes.
	 */
	protected void onBefore() {
	}

	/**
	 * Invoked on the background thread to do the work.
	 * 
	 * @return The result to be passed to onSuccess.
	 */
	protected abstract Result call() throws Exception;

	/**
	 * Called on the UI thread when {@link #call()} completes successfully and
	 * the task has not been cancelled.
	 * 
	 * @param result
	 *            the value returned by {@link #call()}
	 */
	protected void onSuccess(Result result) {
	};

	/**
	 * Called on the UI thread when {@link #call()} throws an exception. This is
	 * called whether or not the task has been cancelled.
	 * 
	 * @param ex
	 *            the exception thrown
	 */
	protected void onException(Exception ex) {
	}

	/**
	 * Called on the UI thread when {@link #call()} has completed successfully
	 * but {@link #cancel(boolean)} has not. Note that this is <em>not</em>
	 * called if {@link #call()} threw an exception.
	 */
	protected void onCancelled() {
	}

	/**
	 * Called on the UI thread after the main callback (
	 * {@link #onSuccess(Object)}, {@link #onException(Exception)}, or
	 * {@link #onCancelled()}) has been called. This method is invoked whether
	 * even if the callback threw an exception (which is rethrown after this
	 * method is invoked).
	 */
	protected void onFinally() {
	}

}
