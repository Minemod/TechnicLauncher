package org.spoutcraft.launcher.technic;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.spoutcraft.launcher.exceptions.RestfulAPIException;

public class TechnicRestAPI {

	private static final ObjectMapper mapper = new ObjectMapper();

	public static final String REST_URL = "http://www.sctgaming.com/Technic/API/";
	public static final String MODPACKS_URL = REST_URL + "modpack/";
	public static final String CACHE_URL = REST_URL + "cache/";
	public static final String MOD_URL = CACHE_URL + "mod/";
	public static final String MIRROR_URL = "http://mirror.technicpack.net/Technic/";

	public static String getModDownloadURL(String mod, String build) {
		return MIRROR_URL + "mods/" + mod + "/" + mod + "-" + build + ".zip";
	}

	public static String getModMD5URL(String mod, String build) {
		return CACHE_URL + "mod/" + mod + "/" + build + "/MD5";
	}

	public static String getModpackURL(String modpack, String build) {
		return MODPACKS_URL + modpack + "/build/" + build;
	}

	public static String getModpackMD5URL(String modpack) {
		return MODPACKS_URL + modpack + "/MD5";
	}

	public static String getModpackYMLURL(String modpack) {
		return MIRROR_URL + modpack + "/modpack.yml";
	}

	public static String getModpackBuildsURL(String modpack) {
		return MODPACKS_URL + modpack;
	}

	public static List<ModpackBuilds> getModpacks() throws RestfulAPIException {
		InputStream stream = null;
		String url = MODPACKS_URL;
		try {
			URL conn = new URL(url);
			stream = conn.openConnection().getInputStream();
			Modpacks result = mapper.readValue(stream, Modpacks.class);
			return result.getModpacks();
		} catch (IOException e) {
			throw new RestfulAPIException("Error accessing URL [" + url + "]", e);
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}

	public static String getModMD5(String mod, String build) throws RestfulAPIException {
		InputStream stream = null;
		String url = getModMD5URL(mod, build);
		try {
			URL conn = new URL(url);
			stream = conn.openConnection().getInputStream();
			TechnicMD5 md5Result = mapper.readValue(stream, TechnicMD5.class);
			return md5Result.getMD5();
		} catch (IOException e) {
			throw new RestfulAPIException("Error accessing URL [" + url + "]", e);
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}

	public static Modpack getModpack(String modpack, String build) throws RestfulAPIException {
		InputStream stream = null;
		String url = getModpackURL(modpack, build);
		try {
			URL conn = new URL(url);
			stream = conn.openConnection().getInputStream();
			Modpack result = mapper.readValue(stream, Modpack.class);
			return result.setInfo(modpack, build);
		} catch (IOException e) {
			throw new RestfulAPIException("Error accessing URL [" + url + "]", e);
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}

	public static ModpackBuilds getModpackBuilds(String modpack) throws RestfulAPIException {
		InputStream stream = null;
		String url = getModpackBuildsURL(modpack);
		try {
			URL conn = new URL(url);
			stream = conn.openStream();
			ModpackBuilds result = mapper.readValue(stream, ModpackBuilds.class);
			return result;
		} catch (IOException e) {
			throw new RestfulAPIException("Error accessing URL [" + url + "]", e);
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}

	public static String getLatestBuild(String modpack) throws RestfulAPIException {
		return getModpackBuilds(modpack).getLatest();
	}

	public static String getRecommendedBuild(String modpack) throws RestfulAPIException {
		return getModpackBuilds(modpack).getRecommended();
	}

	public static String getModpackMD5(String modpack) throws RestfulAPIException {
		InputStream stream = null;
		String url = getModpackMD5URL(modpack);
		try {
			URL conn = new URL(url);
			stream = conn.openConnection().getInputStream();
			TechnicMD5 md5Result = mapper.readValue(stream, TechnicMD5.class);
			return md5Result.getMD5();
		} catch (IOException e) {
			throw new RestfulAPIException("Error accessing URL [" + url + "]", e);
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}

//	private static InputStream getJSONStream(URL url) throws RestfulAPIException {
//		InputStream stream = null;
//		try {
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			conn.setRequestProperty("Content-Type", "application/json");
//			conn.setRequestMethod("GET");
//			conn.connect();
//			stream = conn.getInputStream();
//			return stream;
//		} catch (IOException e) {
//			throw new RestfulAPIException("Error accessing URL [" + url.getPath() + "]", e);
//		} finally {
//			IOUtils.closeQuietly(stream);
//		}
//	}
	private static class TechnicMD5 {
		@JsonProperty("MD5")
		String md5;

		public String getMD5() {
			return md5;
		}
	}
}