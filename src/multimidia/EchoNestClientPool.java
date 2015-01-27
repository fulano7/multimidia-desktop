package multimidia;

import java.util.HashMap;

import com.echonest.api.v4.EchoNestAPI;

public final class EchoNestClientPool {
	private final ThreadLocal<EchoNestAPI> mClients;

	public EchoNestClientPool(final HashMap<Integer, String> keys) {
		mClients = new ThreadLocal<EchoNestAPI>() {
			@Override
			protected final EchoNestAPI initialValue() {
				final Thread thread = Thread.currentThread();

				final int id = Integer.valueOf(thread.getName().replace("AnalysisThread#", ""));

				final String apiKey = keys.get(id);

				return new EchoNestAPI(apiKey);
			}
		};
	}

	public final EchoNestAPI currentClient() {
		return mClients.get();
	}
}