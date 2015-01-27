package multimidia;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ExecutorsUtil {
	private ExecutorsUtil() { }

	public static final ExecutorService newAnalysisThreadPool(int apiKeyCount) {
		return Executors.newFixedThreadPool(apiKeyCount, new AnalysisThreadFactory());
	}
}