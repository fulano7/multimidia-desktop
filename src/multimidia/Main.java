package multimidia;

import java.io.File;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.threeten.bp.Duration;
import org.threeten.bp.Instant;

import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.Track;
import com.echonest.api.v4.Track.AnalysisStatus;

class Triple<T1, T2, T3> {
	final T1 first;
	final T2 second;
	final T3 third;

	Triple(T1 first, T2 second, T3 third) {
		this.first = first;

		this.second = second;
		
		this.third = third;
	}
}

class IncrementalThreadFactory implements ThreadFactory {
	private final AtomicInteger mThreadId;

	public IncrementalThreadFactory() {
		mThreadId = new AtomicInteger();
	}

	@Override
	public Thread newThread(Runnable r) {
		final Thread thread = Executors.defaultThreadFactory().newThread(r);
		thread.setName("AnalysisThread#" + mThreadId.incrementAndGet());

		return thread;
	}
}

class EchoNestClientPool {
	private final ThreadLocal<EchoNestAPI> mClients;

	public EchoNestClientPool(final HashMap<Integer, String> keys) {
		mClients = new ThreadLocal<EchoNestAPI>() {
			@Override
			protected EchoNestAPI initialValue() {
				final Thread thread = Thread.currentThread();

				final int id = Integer.valueOf(thread.getName().replace("AnalysisThread#", ""));

				final String apiKey = keys.get(id);

				return new EchoNestAPI(apiKey);
			}
		};
	}

	public EchoNestAPI currentClient() {
		return mClients.get();
	}
}

public class Main {
	public static void main(String[] args) throws InterruptedException, ExecutionException, EchoNestException {
		final Instant begin = Instant.now();

		final HashMap<Integer, String> apiKeys = new HashMap<>(4, 1.0F);
		apiKeys.put(1, "CEPQCRBVQ1T9NZBIV ");
		apiKeys.put(2, "RVZCDSYGXJSLVIBYD ");
		apiKeys.put(3, "AURMV2OX6ZAGVRXFN ");
		apiKeys.put(4, "B8JTSHJGCDLFTCPKM ");

		final File musicDir = new File("C:\\Users\\HoracioFilho\\Music"); // Diretório sensível.

		if (musicDir.exists() && musicDir.isDirectory()) {
			final Collection<File> musicFilesFound = FileUtils.listFiles(musicDir, new RegexFileFilter(".*\\.(mp3)$"), TrueFileFilter.INSTANCE);
			
			final PriorityQueue<File> musicFiles = new PriorityQueue<File>(musicFilesFound.size(), new Comparator<File>() {
				@Override
				public int compare(File left, File right) {
					final long leftLength = left.length(); 
					final long rightLength = right.length();
					
					if (leftLength > rightLength) {
						return 1;
					} else if (leftLength < rightLength) {
						return -1;
					} else {
						return 0;
					}
				}
			});
			musicFiles.addAll(musicFilesFound);
			
			System.out.println("Quantidade de músicas encontradas: " + musicFiles.size());

			final AtomicInteger musicCount = new AtomicInteger();
			
			final EchoNestClientPool clients = new EchoNestClientPool(apiKeys);

			final ExecutorService threadPool = Executors.newFixedThreadPool(apiKeys.size(), new IncrementalThreadFactory());

			final ExecutorCompletionService<Triple<File, Track, Integer>> pool = new ExecutorCompletionService<>(threadPool);

			while (musicFiles.size() > 0) {
				final File music = musicFiles.poll();
				
				if (music.exists()) {
					final int order = musicCount.incrementAndGet();

					pool.submit(new Callable<Triple<File, Track, Integer>>() {
						@Override
						public Triple<File, Track, Integer> call() throws Exception {
							final EchoNestAPI client = clients.currentClient();

							Triple<File, Track, Integer> analysis = null;

							boolean failed;

							do {
								failed = false;

								try {
									final Instant now = Instant.now();
									
									System.out.println(String.format("Starting upload - %dº music", order));
									
									final Track track = client.uploadTrack(music);
									track.waitForAnalysis(1000L);

									System.out.println(String.format("Uploaded after %s seconds - %dº music", Duration.between(now, Instant.now()).getSeconds(), order));
									
									if (track.getStatus() == AnalysisStatus.COMPLETE) {
										analysis = new Triple<File, Track, Integer>(music, track, order);
									} else {
										musicCount.getAndDecrement();
									}
								} catch (EchoNestException e) {
									System.out.println(String.format("Error: %s. Trying again - %dº music", e.getMessage(), order));
									
									failed = true;
								}
							} while (failed);

							return analysis;
						}
					});
				}
			}

			while (musicCount.getAndDecrement() > 0) {
				final Triple<File, Track, Integer> analysis = pool.take().get();

				if (analysis != null) {
					System.out.println(String.format("Successful - %dº music", analysis.third));
				}
			}
			
			threadPool.shutdownNow();
		}

		System.out.println(String.format("Ended after %s seconds", Duration.between(begin, Instant.now()).getSeconds()));
	}
}