
import java.io.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import myfileio.MyFileIO;

// TODO: Auto-generated Javadoc
/**
 * The Class MyFileIO_Tester.
 */
public class MyFileIO_Tester {

	/**
	 * The main method. 
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		/* You can use this main to test your MyFileIO methods...
		   For example, you might want to get a file handle for a file
		   that you know does not exist, check the various methods that File
		   has, and check what your checkTextFile returns.
		   Then create a new empty file - is it there? does it have the right size?
		   then delete it...was it deleted?
		   Then write a new file (in output/ directory), save and close,
		   then read it... are the contents correct... then delete the file...
		   was it deleted?
		 */
		//CREATE A FILE BEFORE TESTING, WILL THROW NULL POINTER EXCEPTION 
		String filename = "testfile";
		File newFile = new File(filename);
		FileReader fr;
		FileWriter fw;
		BufferedReader br;
		BufferedWriter bw;
		
		MyFileIO fio = new MyFileIO();

		//getFileHandle, createEmptyFile, deleteFile, fileStatus
		try {
			fio.createEmptyFile(filename);
			newFile = fio.getFileHandle(filename);
			if(newFile.exists()) 
				System.out.println("File exists");
			if(newFile.isFile()) 
				System.out.println("File, not directory");
			if(fio.checkFileStatus(newFile, true) == MyFileIO.READ_ZERO_LENGTH) 
				System.out.println("File is empty and is of correct status");
			fr = fio.openFileReader(newFile);
			fw = fio.openFileWriter(newFile);
			String read = "abc", read2 = "bcdsd";
			fw.write(read);
			read += (char) fr.read();

			System.out.println(read + " word in file");
			fio.closeFile(fr);
			fio.closeFile(fw);
			
			br = fio.openBufferedReader(newFile);
			bw = fio.openBufferedWriter(newFile);
			bw.write(read2);
			read2 += (char) br.read();
			
			System.out.println(read2 + " word in file");
			fio.closeFile(br);
			fio.closeFile(bw);
			if(fio.deleteFile(filename)) {
				if(!newFile.exists() && !newFile.canWrite()) 
					System.out.println("File delete works");
			}

		} catch (Exception e) {
			System.out.println("Exception occured");
			e.printStackTrace();
		}





	}

}
