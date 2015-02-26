package multimidia;

import java.io.File;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Vector;
import java.util.concurrent.Callable;
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

public class Principal {
	public volatile static Vector<Musica> listaMusicas =  new Vector<Musica>();
	
	public static void echonestMagic(String diretorio) throws Exception {
		final Instant begin = Instant.now();

		int analysisCount = 0;
		
		final HashMap<Integer, String> apiKeys = new HashMap<>(5, 1.0F);
		apiKeys.put(1, "CEPQCRBVQ1T9NZBIV ");
		apiKeys.put(2, "RVZCDSYGXJSLVIBYD ");
		apiKeys.put(3, "AURMV2OX6ZAGVRXFN ");
		apiKeys.put(4, "B8JTSHJGCDLFTCPKM ");
		apiKeys.put(5, "3FWTLRQV62Z6CIG4B ");
		
		
		final File musicDir = new File(diretorio); 
		int valencia = -1;
		int ativacao = -1;
		
	
		String modelValencia = "arquivos/modelValencia"; 
		String attributeSelectionValencia = "arquivos/attributeSelectionValencia";
		String modelAtivacao= "arquivos/modelAtivacao"; 
		String attributeSelectionAtivacao = "arquivos/attributeSelectionAtivacao";

		if (musicDir.exists() && musicDir.isDirectory()) {
			final PriorityQueue<File> musicFiles = (PriorityQueue<File>) FileUtils.listFiles(musicDir, new RegexFileFilter(".*\\.(mp3)$"), TrueFileFilter.INSTANCE);
			
			final AtomicInteger musicCount = new AtomicInteger();
			
			System.out.println("Quantidade de músicas encontradas: " + musicFiles.size());
			if(musicFiles.size()==0) throw new NoMusicFoundException(diretorio);
			
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
			
			
				while (musicCount.getAndDecrement() > 0) {
					final EchoNestAnalysis analysis = pool.take().get();
	
					if (analysis.track != null) {
						System.out.println(String.format("Successful - %d music", analysis.order));
						
						final File music = analysis.music;
						
						final Track track = analysis.track;
						
						String a = String.format("%s,%s,%s,%s,%s,?,%s", track.getTempo(), track.getDanceability(), track.getSpeechiness(), track.getLiveness(), track.getEnergy(), track.getTitle());
						System.out.println(a);
				
						
						String responseValencia = Classify.classifySingleUnlabeledInstance(new MusicInstance(track.getTempo(), track.getDanceability(), track.getSpeechiness(), track.getLiveness(), track.getEnergy()),modelValencia, attributeSelectionValencia);
						String responseAtivacao = Classify.classifyActivationSingleUnlabeledInstance(new MusicInstance(track.getTempo(), track.getDanceability(), track.getSpeechiness(), track.getLiveness(), track.getEnergy()),modelAtivacao, attributeSelectionAtivacao);
						
						System.out.println("musica : " + music.getName() + ", Valência : " + responseValencia + ", Ativacao : " + responseAtivacao + ", caminho : "+ music.getAbsolutePath());
						
						
						if (responseValencia.equals("alegre")){
							valencia = 1;
						}else if(responseValencia.equals("triste")){
							valencia = 0;
						}
						
						if (responseAtivacao.equals("calmo")){
							ativacao = 0;
						}else if(responseAtivacao.equals("agitado")){
							ativacao = 1;
						}
						
						Musica m = new Musica(music.getName(), music.getAbsolutePath(),valencia, ativacao);
						listaMusicas.add(m);
						
						
					} else {
						System.out.println(String.format("Unsuccessful - %d music", analysis.order));
					}
					
					analysisCount = analysisCount + 1;
				}
			
			
			threadPool.shutdownNow();
		}
		
		
		System.out.println(String.format("Ended after %s seconds - %d analysis", Duration.between(begin, Instant.now()).getSeconds(), analysisCount));
	}
	
}