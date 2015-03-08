import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

public class hdfsDAO {

	private static String hdfs = "";
	private String path;
	private Configuration conf;

	public hdfsDAO(Configuration conf) {
		this.conf = conf;
	}

	public hdfsDAO(String path, Configuration conf) {
		super();
		this.path = path;
		this.conf = conf;
	}

	public void mkdir(String folder) throws Exception {
		Path createFolder = new Path(folder);
		FileSystem fs = FileSystem.get(new URI(path), conf);
		if (!fs.exists(createFolder)) {
			fs.mkdirs(createFolder);
		}
		fs.close();
		System.out.println("create  " + createFolder);
	}

	public void rmr(String folder) throws Exception {
		Path createFolder = new Path(folder);
		FileSystem fs = FileSystem.get(new URI(path), conf);
		fs.deleteOnExit(createFolder);
		fs.close();
		System.out.println("delete " + createFolder);
	}

	public void rename(String src, String dst) throws Exception {
		Path name1 = new Path(src);
		Path name2 = new Path(dst);
		FileSystem fs = FileSystem.get(new URI(path), conf);
		fs.rename(name1, name2);
		System.out.println("rename from " + src + " to " + dst);
		fs.close();
	}

	public void ls(String lsPath) throws Exception {
		Path ls = new Path(lsPath);
		FileSystem fs = FileSystem.get(new URI(path), conf);
		FileStatus[] list = fs.listStatus(ls);
		System.out.println("ls " + ls);
		for (FileStatus f : list) {
			System.out.printf("name: %s, folder: %s, size: %d\n", f.getPath(),
					f.isDir(), f.getLen());
		}
		fs.close();
	}

	public void createFile(String fileName, String filePath) throws Exception {
		FileSystem fs=FileSystem.get(new URI(path),conf);
		Path createPath=new Path(fileName);
		FSDataOutputStream dataOutputStream=fs.create(createPath);
		FileInputStream fileInputStream=new FileInputStream(filePath);
		IOUtils.copyBytes(fileInputStream, dataOutputStream, conf);
		fs.close();
	}
	public void uploadFile(String local,String remote) throws Exception{
		FileSystem fs=FileSystem.get(new URI(path),conf);
		fs.copyFromLocalFile(new Path(local), new Path(remote));
		
		System.out.println("copy file from "+local+" to "+remote);
		fs.close();
	}
	public void download(String remote,String local) throws Exception{
		FileSystem fs=FileSystem.get(new URI(path),conf);
		fs.copyToLocalFile(new Path(remote), new Path(local));
		fs.close();
		System.out.println("download file"+remote +"to "+local);
	}
	static String fs="hdfs://192.168.121.200:9000/";
	public static void main(String[] args) throws Exception {
		Configuration conf=new Configuration();
		hdfsDAO HDFS=new hdfsDAO(fs, conf);
//		HDFS.mkdir("/songxupeng");
//		HDFS.rmr("/songxupeng");
//		HDFS.ls("/");
//		HDFS.createFile("/songxupeng", "c:/conf/core-site.xml");
		HDFS.uploadFile("c:/conf/core-site.xml", "/XXXX");
	}

}
