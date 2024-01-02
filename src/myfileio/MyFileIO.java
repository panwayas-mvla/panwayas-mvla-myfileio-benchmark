package myfileio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

// TODO: Auto-generated Javadoc
/**
 * The Class MyFileIO.
 */
public class MyFileIO {

	/* checkTextFile return values - do not change!!! */
	public static final int FILE_OK=0;
	public static final int EMPTY_NAME=1;
	public static final int NOT_A_FILE = 2;
	public static final int FILE_DOES_NOT_EXIST=3;
	public static final int READ_ZERO_LENGTH=4;
	public static final int NO_READ_ACCESS=5;
	public static final int NO_WRITE_ACCESS=6;
	public static final int WRITE_EXISTS=7;      

	public static void main (String args[]) {
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
	
	/**
	 * Returns the File descriptor (Handle) for the given path.
	 *
	 * @param filename  the filename, including relative or absolute path
	 * @return  File object which contains information about the file
	 */
	public File getFileHandle (String filename) {
		if(filename == null) return null;
		
		return (new File(filename));
	}

	/**
	 * Creates an empty file, if it is safe to do so
	 * Pre-checks to be performed
	 *  1) check for an empty filename
	 *  2) check if the file exists 
	 *  If these checks occur, the file will not be created.
	 *  Otherwise, returns the result of the File createNewFile() method
	 *  This of course means that you need to get the File handle!
	 *  
	 *  Errors are not expected (given 1 and 2), but it is possible that
	 *  this method will generate an IOException or SecurityException.
	 *  Both should be caught here: the catch should print out a message
	 *  to the console indicating that the error occurred, and then 
	 *  call e.printStackTrace();
	 *
	 * @param filename the filename
	 * @return true, if successful
	 */
	public boolean createEmptyFile(String filename) {
		boolean status = false;
		if(filename == null || filename.isEmpty()) return status;
		if(!getFileHandle(filename).exists()) {
			try {
				File newFile = new File(filename);
				newFile.createNewFile();
				status = true;
			} catch (Exception e) {
				System.out.println("Exception " + e + " has occured");
				e.printStackTrace();
			}
		}
		return status;
	}

	/**
	 * Delete file.  Uses the File delete() method to delete a file
	 * Pre-checks:
	 * 1) the filename must not be empty
	 * 2) file must exist
	 * 3) file must actually be a file
	 * If these conditions are not met, the delete() should not be attempted
	 * 
	 *  Errors are not expected, but it is possible that
	 *  this method will generate a SecurityException.
	 *  This should be caught here: the catch should print out a message
	 *  to the console indicating that the error occurred, and then 
	 *  call e.printStackTrace();
	 *
	 * @param filename the filename
	 * @return true, if successful
	 */
	public boolean deleteFile(String filename) {
		boolean status = false;
		if(filename == null || filename.isEmpty()) return status;
		if(getFileHandle(filename).exists()) {
			try {
				File fileHandle = new File(filename);
				if(fileHandle.isDirectory()) return status;
				fileHandle.delete();
				status = true;
			} catch (Exception e) {
				System.out.println("Exception " + e + " has occured");
				e.printStackTrace();
			}
		}
		return status;	
	}	

	/**
	 * This method checks information about the file handle 
	 * to determine if it is safe to read or write the file,
	 * based upon the requested access type.
	 * 
	 * If the name of the file is empty, return the appropriate
	 * error code - this does NOT depend upon the value of read
	 * 
	 * Conditions to read the file:
	 *   a) file exists
	 *   b) file is indeed a file
	 *   c) file has data
	 *   d) file has read access
	 *   
	 * Conditions to write the file:
	 *   a) file does not exist 
	 *   b) file exists, is writable, and is a file. Note that in this
	 *      case, you should should return WRITE_EXISTS to indicate
	 *      that the file can be written but does already exist.
	 *      
	 * If the conditions are not met, you should return the correct failure
	 * status. 
	 *
	 * @param file is the File descriptor. 
	 * @param read if true, perform the read checks; if false,
	 *        perform the write checks
	 * @return the results of the status checks. These are the
	 *        constants defined at the top of the file.
	 */
	public int checkFileStatus(File file, boolean read) {
		String fileName = file.getName();
		if(fileName == null || fileName.isEmpty()) return EMPTY_NAME;
		if(!file.exists()) {
			if(read) return FILE_DOES_NOT_EXIST;
			if(!read) return FILE_OK;
		}
		if(file.exists() && !read && file.isFile() && file.canWrite()) return WRITE_EXISTS;
		if(!file.isFile()) return NOT_A_FILE;
		if(!read && !file.canWrite()) return NO_WRITE_ACCESS;
		if(read && !file.canRead()) return NO_READ_ACCESS;
		if(file.length() == 0) return READ_ZERO_LENGTH;
		return FILE_OK;
	}

	/**
	 * Open a FileReader object for this File handle. 
	 * 
	 * Any exceptions should be caught here, but should NOT
	 * terminate - just return a null pointer.
	 *
	 * @param file the file handle
	 * @return FileReader object if successful, otherwise null
	 */
	public FileReader openFileReader(File file) {
		FileReader fr = null;
		try {
			int status = checkFileStatus(file, true);
			if(status == FILE_OK || status == READ_ZERO_LENGTH)
				fr = new FileReader(file);
		} catch (Exception e) {
			System.out.println("Exception " + e + " has occured");
			e.printStackTrace();
		}
		return fr;
	}

	/**
	 * Open FileWriter object for this File handle. 
	 * 
	 * Any exceptions should be caught here, but should NOT
	 * terminate - just return a null pointer.
	 *
	 * @param file the file handle
	 * @return FileReader object if successful, otherwise null
	 */
	public FileWriter openFileWriter(File file) {
		FileWriter fw = null;
		try {
			int status = checkFileStatus(file, false);
			if(status == FILE_OK || status == WRITE_EXISTS)
				fw = new FileWriter(file);
		} catch (Exception e) {
			System.out.println("Exception " + e + " has occured");
			e.printStackTrace();
		}
		return fw;
	}

	/**
	 * Open BufferedReader object for this File handle. 
	 *
	 * @param file the file handle
	 * @return BufferedReader object if successful, otherwise null
	 */
	public BufferedReader openBufferedReader(File file) {
		BufferedReader br = null;
		try {
			int status = checkFileStatus(file, true);
			if(status == FILE_OK || status == READ_ZERO_LENGTH)
				br = new BufferedReader(openFileReader(file));
		} catch (Exception e) {
			System.out.println("Exception " + e + " has occured");
			e.printStackTrace();
		}
		return br;
	}

	/**
	 * Open BufferedWriter object for this File handle. 
	 *
	 * @param file the file handle
	 * @return BufferedWriter object if successful, otherwise null
	 */
	public BufferedWriter openBufferedWriter(File file) {
		BufferedWriter bw = null;
		try {
			int status = checkFileStatus(file, false);
			if(status == FILE_OK || status == WRITE_EXISTS) 
				bw = new BufferedWriter(openFileWriter(file));
		} catch (Exception e) {
			System.out.println("Exception " + e + " has occured");
			e.printStackTrace();
		}
		return bw;
	}

	/**
	 * Close file. 
	 *  Catches IOException if it occurs - output an error message, then 
	 *  e.printStackTrace
	 *
	 * @param fr the FileReader object
	 */
	public void closeFile(FileReader fr) {
		if(fr == null) return; 
		try {
			fr.close();
		} catch (Exception e) {
			System.out.println("Exception " + e + " has occured");
			e.printStackTrace();
		}
	}

	/**
	 * Flush and Close file. 
	 *  Catches IOException if it occurs - output an error message, then 
	 *  e.printStackTrace
	 *
	 * @param fw the FileWriter object
	 */
	public void closeFile(FileWriter fw) {
		if(fw == null) return; 
		try {
			fw.flush();
			fw.close();
		} catch (IOException e) {
			System.out.println("Exception " + e + " has occured");
			e.printStackTrace();
		}
	}

	/**
	 * Close file. 
	 *  Catches IOException if it occurs - output an error message, then 
	 *  e.printStackTrace
	 *
	 * @param br the BufferedReader object
	 */
	public void closeFile(BufferedReader br) {
		if(br == null) return; 
		try {
			br.close();
		} catch (Exception e) {
			System.out.println("Exception " + e + " has occured");
			e.printStackTrace();
		}
	}

	/**
	 * Flush and Close file. 
	 *  Catches IOException if it occurs - output an error message, then 
	 *  e.printStackTrace
	 *
	 * @param bw the BufferedWriter object
	 */
	public void closeFile(BufferedWriter bw) {
		if(bw == null) return; 
		try {
			bw.flush();
			bw.close();
		} catch (IOException e) {
			System.out.println("Exception " + e + " has occured");
			e.printStackTrace();
		}
	}

}
