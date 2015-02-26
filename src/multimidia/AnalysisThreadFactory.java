package multimidia;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public final class AnalysisThreadFactory implements ThreadFactory {
	private final AtomicInteger mThreadId;

	public AnalysisThreadFactory() {
		mThreadId = new AtomicInteger();
	}

	@Override
	public final Thread newThread(Runnable r) {
		Thread thread = Executors.defaultThreadFactory().newThread(r);
		thread.setName("AnalysisThread#" + mThreadId.incrementAndGet());
		thread.setDaemon(true);
		return thread;
	}
}