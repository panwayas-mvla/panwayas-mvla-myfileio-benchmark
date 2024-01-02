import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import myfileio.MyFileIO;

// TODO: Auto-generated Javadoc
/**
 * The Class BenchmarkFileAccess.
 */
public class BenchmarkFileAccess {

	/** The Constant NUM_FILES. */
	public static final int NUM_FILES = 5;

	/** The Constant NUM_ITER. */
	public static final int NUM_ITER = 10;

	/** The Constant CHARS_PER_FILE. */
	public static final int[] CHARS_PER_FILE = {100,1000,10000,100000,1000000};

	/** The file IO. */
	private MyFileIO fileIO = new MyFileIO();

	/** The results. */
	private long[][] results = new long[NUM_FILES][NUM_ITER];

	/**
	 * clears the 2D array of results.... called at the start of each
	 * benchmark method.
	 */
	private void initResults() {
		for (int fileNum = 0; fileNum < NUM_FILES; fileNum++) {
			for (int iter =0; iter < NUM_ITER; iter++) {
				results[fileNum][iter]=0;
			}
		}
	}

	/**
	 * Benchmark file reader.
	 * Tests the performance of FileReader across 5 files of size
	 * 100, 1000, 10000, 100000, and 1000000 characters. The performance
	 * measurements are repeated 10 times to get a sense of repeatability
	 * and sensitivity to system variance. Uses System.nanoTime() to timestamp
	 * time in nanoseconds around tight loop of reads.
	 * 
	 */
	private void benchmarkFileReader() {
		long start=0, stop=0;
		initResults();
		System.out.println("Executing FileReader Benchmark");
		for (int fileNum = 0; fileNum < NUM_FILES; fileNum++ ) {
			File fh = fileIO.getFileHandle("data/test_"+fileNum);
			System.out.println("Benchmarking "+fh.getName()+":");
			if (fileIO.checkFileStatus(fh, true) == MyFileIO.FILE_OK) {
				long fileLength = fh.length();
				for (int iter = 0; iter < NUM_ITER; iter++) {
					System.out.println("   Iteration: "+iter);
					FileReader fr = fileIO.openFileReader(fh);
					try {
						start = System.nanoTime();
						for (int charCnt = 0; charCnt < fileLength; charCnt++) {
							fr.read();
						}
						stop = System.nanoTime();
					} catch (IOException e) {
						System.out.println("IO Exception occurred while reading data/text_"+fileNum);
						e.printStackTrace();
					}
					fileIO.closeFile(fr);
					results[fileNum][iter] = stop - start;
				}
			}
		}
	}

	/**
	 * Benchmark buffered reader. You need to write this -
	 * Essentially, the same code as benchmarkFileReader, but
	 * using the BufferedReader instead.
	 */
	private void benchmarkBufferedReader() {
		long start=0, stop=0;
		initResults();
		System.out.println("Executing BufferedReader Benchmark");
		for (int fileNum = 0; fileNum < NUM_FILES; fileNum++ ) {
			File fh = fileIO.getFileHandle("data/test_"+fileNum);
			System.out.println("Benchmarking "+fh.getName()+":");
			if (fileIO.checkFileStatus(fh, true) == MyFileIO.FILE_OK) {
				long fileLength = fh.length();
				for (int iter = 0; iter < NUM_ITER; iter++) {
					System.out.println("   Iteration: "+iter);
					BufferedReader br = fileIO.openBufferedReader(fh);
					try {
						start = System.nanoTime();
						for (int charCnt = 0; charCnt < fileLength; charCnt++) {
							br.read();
						}
						stop = System.nanoTime();
					} catch (IOException e) {
						System.out.println("IO Exception occurred while reading data/text_"+fileNum);
						e.printStackTrace();
					}
					fileIO.closeFile(br);
					results[fileNum][iter] = stop - start;
				}
			}
		}
	}

	/**
	 * Benchmark file writer. Same concept as for benchmarking FileReader,
	 * except:
	 * 1) cannot use file length as the loop comparison; use the CHARS_PER_FILE constant...
	 * 2) you can write any single character you want...
	 * 3) after you close the file, you should delete it using the methods in fileIO....
	 */
	private void benchmarkFileWriter() {
		long start = 0, stop = 0;
		initResults();
		System.out.println("Executing FileWriter Benchmark");
		for(int fileNum = 0; fileNum < NUM_FILES; fileNum++) {
			File fh = fileIO.getFileHandle("output/test_" + fileNum);
			System.out.println("Benchmarking" + fh.getName() + ":");
			if (fileIO.checkFileStatus(fh, false) == MyFileIO.FILE_OK) {
				for(int iter = 0; iter < NUM_ITER; iter++) {
					System.out.println("   Iteration: " + iter);
					FileWriter fw = fileIO.openFileWriter(fh);
					try {
						start = System.nanoTime();
						for(int charCount = 0; charCount < CHARS_PER_FILE[fileNum]; charCount++) {
							fw.write('a');
						}
						stop = System.nanoTime();
					} catch (Exception e) {
						System.out.println("Exception occurred while reading data/text_"+fileNum);
						e.printStackTrace();
					}
					fileIO.closeFile(fw);
					fileIO.deleteFile(fh.getPath()); 
					results[fileNum][iter] = stop - start;
				}
			}
		}
	}

	/**
	 * Benchmark buffered writer. Same as benchmarkFileWriter, but
	 * using BufferedWriter instead of FileWriter
	 */
	private void benchmarkBufferedWriter() {
		long start = 0, stop = 0;
		initResults();
		System.out.println("Executing BufferedWriter Benchmark");
		for(int fileNum = 0; fileNum < NUM_FILES; fileNum++) {
			File fh = fileIO.getFileHandle("output/test_" + fileNum);
			System.out.println("Benchmarking" + fh.getName() + ":");
			if (fileIO.checkFileStatus(fh, false) == MyFileIO.FILE_OK) {
				for(int iter = 0; iter < NUM_ITER; iter++) {
					System.out.println("   Iteration: " + iter);
					BufferedWriter bw = fileIO.openBufferedWriter(fh);
					try {
						start = System.nanoTime();
						for(int charCount = 0; charCount < CHARS_PER_FILE[fileNum]; charCount++) {
							bw.write('a');
						}
						stop = System.nanoTime();
					} catch (Exception e) {
						System.out.println("Exception occurred while reading data/text_"+fileNum);
						e.printStackTrace();
					}
					fileIO.closeFile(bw);
					fileIO.deleteFile(fh.getPath()); 
					results[fileNum][iter] = stop - start;
				}
			}
		}
	}	

	/**
	 * Prints the results to the console AND
	 * to output/<benchmark>.csv so that you can load into
	 * a spreadsheet for analysis.
	 *
	 * @param benchmark the benchmark being printed
	 */
	private void printResults(String benchmark) {
		String str = "";
		str += "\nBenchmark: "+benchmark+"\n";
		str += "Test File";
		for (int iter = 0; iter < NUM_ITER; iter++) {
			str += ","+iter;
		}
		str += "\n";
		for (int fileNum = 0; fileNum < NUM_FILES; fileNum++) {
			str += "data/test_"+fileNum;
			for (int iter = 0; iter < NUM_ITER; iter++) {
				str += ","+results[fileNum][iter];
			}
			str += "\n";
		}
		System.out.print(str);
		File fh = fileIO.getFileHandle("output/"+benchmark+".csv");
		int status = fileIO.checkFileStatus(fh, false);
		if ((status == MyFileIO.FILE_OK) || (status == MyFileIO.WRITE_EXISTS)) {
			BufferedWriter bw = fileIO.openBufferedWriter(fh);
			try {
				bw.write(str);
			}
			catch (IOException e) {
				System.out.println("IO Exception while writing file: "+fh.getPath());
				e.printStackTrace();
			}
			fileIO.closeFile(bw);
		}
	}


	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		BenchmarkFileAccess bmfa = new BenchmarkFileAccess();
		bmfa.benchmarkFileReader();
		bmfa.printResults("FileReader Benchmark");
		bmfa.benchmarkBufferedReader();
		bmfa.printResults("BufferedReader Benchmark");
		bmfa.benchmarkFileWriter();
		bmfa.printResults("FileWriter Benchmark");
		bmfa.benchmarkBufferedWriter();
		bmfa.printResults("BufferedWriter Benchmark");
	}

}
