package me.balaji.sftp.file.poller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.vfs2.FileChangeEvent;
import org.apache.commons.vfs2.FileListener;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.impl.DefaultFileMonitor;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;

/**
 * Hello world!
 *
 */
public class App {
	
	static String hostName = "localhost";
	static String username = "balajivijayan";
	static String password = "";
	static String remoteFilePath = "/Users/balajivijayan/devenv/file-watcher/source";
	
    public static void main( String[] args )
    {Executor runner = Executors.newFixedThreadPool(1);
    runner.execute(new Runnable() {

        public void run() {
        	StandardFileSystemManager manager = new StandardFileSystemManager();

        	try {
				manager.init();
				
			} catch (FileSystemException e1) {
				e1.printStackTrace();
			}
            FileObject listendir = null;
            try {
            	URI uri = null;
                FileSystemManager fsManager = VFS.getManager();
                //listendir = fsManager.resolveFile("/Users/balajivijayan/devenv/file-watcher/source");
                try {
					 uri = new URI("sftp", username + ":" +password, hostName, -1,
					        remoteFilePath, null, null);
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                System.out.println(uri.toString());
				listendir = fsManager.resolveFile(uri.toString(), createDefaultOptions());
            } catch (FileSystemException e) {
                e.printStackTrace();
            }
            DefaultFileMonitor fm = new DefaultFileMonitor(new FileListener() {

                public void fileDeleted(FileChangeEvent event) throws Exception {
                    System.out.println(event.getFile().getName().getPath()+" Deleted.");
                }

                public void fileCreated(FileChangeEvent event) throws Exception {
                    System.out.println( new Date(System.currentTimeMillis()) + event.getFile().getName().getPath()+" Created.");
                }

                public void fileChanged(FileChangeEvent event) throws Exception {
                    System.out.println(event.getFile().getName().getPath()+" Changed.");
                }
            });
            fm.setRecursive(true);
            fm.addFile(listendir);
            fm.start();
        }
    });}
    
    public static FileSystemOptions createDefaultOptions()
            throws FileSystemException {
    // Create SFTP options
    FileSystemOptions opts = new FileSystemOptions();

    // SSH Key checking
    SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(
                    opts, "no");

    // Root directory set to user home
    SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, false);

    // Timeout is count by Milliseconds
    SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);

    return opts;
}
}
