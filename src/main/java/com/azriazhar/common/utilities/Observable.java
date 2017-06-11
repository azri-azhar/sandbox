package com.azriazhar.common.utilities;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

/**
 * A simple implementation of observer pattern without boilerplate code. A
 * listener is executed in a single worker thread operating off an unbounded
 * queue, with a timeout value to cancel the execution. This is to prevent from
 * one listener from blocking other listeners on the list. Listeners are stored
 * in a "snapshot" style thread-safe array.
 *
 * @author Mohamad Azri Azhar
 *
 * @param <T>
 *            the type of object to be passed to observers/listeners upon
 *            notification (the event source).
 */
public class Observable<T> {
	private volatile long listenerExecutionTimeout = 0;
	private List<Consumer<T>> listeners = new CopyOnWriteArrayList<>();
	private ExecutorService executor = Executors.newSingleThreadExecutor();

	public void addListener(final Consumer<T> consumer) {
		if (consumer != null && !listeners.contains(consumer)) {
			listeners.add(consumer);
		}
	}

	public void removeListener(final Consumer<T> consumer) {
		if (consumer != null) {
			listeners.remove(consumer);
		}
	}

	public long getEventExecutionTimeout() {
		return listenerExecutionTimeout;
	}

	public void setEventExecutionTimeout(long listenerExecutionTimeout) {
		this.listenerExecutionTimeout = listenerExecutionTimeout;
	}

	public void notifyListeners(final T event) {
		for (final Consumer<T> consumer : listeners) {
			Future<Void> future = null;

			try {
				future = executor.submit(() -> {
					consumer.accept(event);
					return null;
				});

				future.get(listenerExecutionTimeout, TimeUnit.SECONDS);
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				// Unrecoverable exception, ignore.
			} finally {
				if (listenerExecutionTimeout > 0 && future != null) {
					future.cancel(true);
				}
			}
		}
	}
}
