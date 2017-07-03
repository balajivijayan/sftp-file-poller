package me.balaji.sftp.file.poller;

import org.apache.commons.vfs2.FileChangeEvent;
import org.apache.commons.vfs2.FileListener;

public class CustomFileListener implements FileListener {

	public void fileCreated(FileChangeEvent arg0) throws Exception {
		System.out.println("File created");
	}

	public void fileDeleted(FileChangeEvent arg0) throws Exception {
		System.out.println("File deleted");
	}
	
	public void fileChanged(FileChangeEvent arg0) throws Exception {
		System.out.println("File Changed");
	}

}
