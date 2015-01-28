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

		int analysisCount = 0;
		
		final HashMap<Integer, String> apiKeys = new HashMap<>(5, 1.0F);
		apiKeys.put(1, "CEPQCRBVQ1T9NZBIV ");
		apiKeys.put(2, "RVZCDSYGXJSLVIBYD ");
		apiKeys.put(3, "AURMV2OX6ZAGVRXFN ");
		apiKeys.put(4, "B8JTSHJGCDLFTCPKM ");
		apiKeys.put(5, "3FWTLRQV62Z6CIG4B ");

		final File musicDir = new File("C:\\Users\\HoracioFilho\\Music\\"); // Diretório sensível.

		if (musicDir.exists() && musicDir.isDirectory()) {
			final PriorityQueue<File> musicFiles = (PriorityQueue<File>) FileUtils.listFiles(musicDir, new RegexFileFilter(".*\\.(mp3)$"), TrueFileFilter.INSTANCE);
			
			final AtomicInteger musicCount = new AtomicInteger();
			
			System.out.println("Quantidade de músicas encontradas: " + musicFiles.size());
			
			final EchoNestClientPool clients = new EchoNestClientPool(apiKeys);

			final ExecutorService threadPool = ExecutorsUtil.newAnalysisThreadPool(apiKeys.size());

			final ExecutorCompletionService<EchoNestAnalysis> pool = new ExecutorCompletionService<>(threadPool);

			for (int i = 0, n = musicFiles.size(); i < n; i++) {
				final File music = musicFiles.poll();
				
				if (music.exists()) {
					final int order = i;

					final Callable<EchoNestAnalysis> work = new Callable<EchoNestAnalysis>() {
						@Override
						public EchoNestAnalysis call() throws Exception {
							musicCount.incrementAndGet();
							
							final EchoNestAPI client = clients.currentClient();

							try {
								final Instant now = Instant.now();
									
								System.out.println(String.format("Starting upload of %s - %d music", FileUtils.byteCountToDisplaySize(music.length()), order));
									
								final Track track = client.uploadTrack(music);
								track.waitForAnalysis(30000L);

								System.out.println(String.format("Uploaded after %s seconds - %d music", Duration.between(now, Instant.now()).getSeconds(), order));
									
								if (track.getStatus() == AnalysisStatus.COMPLETE) {
									return new EchoNestAnalysis(music, track, order);
								} 
							} catch (EchoNestException e) {
								System.out.println(String.format("Error: %s. Submitting again - %d music", e.getMessage(), order));
									
								pool.submit(this);
							}

							return new EchoNestAnalysis(music, null, order);
						}
					};
					
					pool.submit(work);
				}
			}
			
			try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(new File("analysis.arff"), true)))
			{
				while (musicCount.getAndDecrement() > 0) {
					final EchoNestAnalysis analysis = pool.take().get();
	
					if (analysis.track != null) {
						System.out.println(String.format("Successful - %d music", analysis.order));
						
						final File music = analysis.music;
						
						final Track track = analysis.track;
						
						fileWriter.append(String.format("%s,%s,%s,%s,%s,%s,?", music.getName(), track.getTempo(), track.getDanceability(), track.getSpeechiness(), track.getLiveness(), track.getEnergy()));
						fileWriter.newLine();
					} else {
						System.out.println(String.format("Unsuccessful - %d music", analysis.order));
					}
					
					analysisCount = analysisCount + 1;
				}
			} catch(IOException e) {
				System.out.println(e.getMessage());
			}
			
			threadPool.shutdownNow();
		}

		System.out.println(String.format("Ended after %s seconds - %d analysis", Duration.between(begin, Instant.now()).getSeconds(), analysisCount));
	}
}