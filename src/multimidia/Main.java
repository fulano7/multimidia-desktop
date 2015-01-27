package multimidia;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
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

public class Main {
	public static void main(String[] args) throws InterruptedException, ExecutionException, EchoNestException, IOException {
		final Instant begin = Instant.now();

		final HashMap<Integer, String> apiKeys = new HashMap<>(4, 1.0F);
		apiKeys.put(1, "CEPQCRBVQ1T9NZBIV ");
		apiKeys.put(2, "RVZCDSYGXJSLVIBYD ");
		apiKeys.put(3, "AURMV2OX6ZAGVRXFN ");
		apiKeys.put(4, "B8JTSHJGCDLFTCPKM ");
		apiKeys.put(5, "3FWTLRQV62Z6CIG4B ");

		final File musicDir = new File("C:\\Users\\hjcf\\Downloads\\Pablo - Perfil - CD 2015"); // Diretório sensível.

		if (musicDir.exists() && musicDir.isDirectory()) {
			final PriorityQueue<File> musicFiles = (PriorityQueue<File>) FileUtils.listFiles(musicDir, new RegexFileFilter(".*\\.(mp3)$"), TrueFileFilter.INSTANCE);
			
			System.out.println("Quantidade de músicas encontradas: " + musicFiles.size());

			final AtomicInteger musicCount = new AtomicInteger();
			
			final EchoNestClientPool clients = new EchoNestClientPool(apiKeys);

			final ExecutorService threadPool = ExecutorsUtil.newAnalysisThreadPool(apiKeys.size());

			final ExecutorCompletionService<Tuple<File, Track, Integer, String>> pool = new ExecutorCompletionService<>(threadPool);

			while (musicFiles.size() > 0) {
				final File music = musicFiles.poll();
				
				if (music.exists()) {
					final int order = musicCount.incrementAndGet();

					pool.submit(new Callable<Tuple<File, Track, Integer, String>>() {
						@Override
						public Tuple<File, Track, Integer, String> call() throws Exception {
							final EchoNestAPI client = clients.currentClient();

							Tuple<File, Track, Integer, String> analysis = null;

							boolean failed;

							do {
								failed = false;

								try {
									final Instant now = Instant.now();
									
									System.out.println(String.format("Starting upload of %s - %d music", FileUtils.byteCountToDisplaySize(music.length()), order));
									
									final Track track = client.uploadTrack(music);
									track.waitForAnalysis(1000L);

									System.out.println(String.format("Uploaded after %s seconds - %d music", Duration.between(now, Instant.now()).getSeconds(), order));
									
									if (track.getStatus() == AnalysisStatus.COMPLETE) {
										String text = String.format("%s,%s,%s,%s,%s,%s,?", music.getName(), track.getTempo(), track.getDanceability(), track.getSpeechiness(), track.getLiveness(), track.getEnergy());
										
										analysis = new Tuple<File, Track, Integer, String>(music, track, order, text);
									} else {
										musicCount.getAndDecrement();
									}
								} catch (Exception e) {
									System.out.println(String.format("Error: %s. Trying again - %d music", e.getMessage(), order));
									
									failed = true;
								}
							} while (failed);

							return analysis;
						}
					});
				}
			}
			
			BufferedWriter fileWriter = new BufferedWriter(new FileWriter(new File("analysis.arff"), true));
			
			while (musicCount.getAndDecrement() > 0) {
				final Tuple<File, Track, Integer, String> analysis = pool.take().get();

				if (analysis != null) {
					System.out.println(String.format("Successful - %d music", analysis.third));
					
					fileWriter.append(analysis.fourth);
					fileWriter.newLine();
				}
			}
			
			fileWriter.close();
			
			threadPool.shutdownNow();
		}

		System.out.println(String.format("Ended after %s seconds", Duration.between(begin, Instant.now()).getSeconds()));
	}
}