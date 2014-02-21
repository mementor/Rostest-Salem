package org.mentor.statreport;

import java.util.Map;
import java.util.HashMap;
import java.lang.Thread;

import java.net.URLConnection;
import java.net.URI;
import java.net.URL;
import java.io.DataOutputStream;
import java.io.InputStream;

import haven.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StatReport {

	private String loginName = "";
	private String charName = "";
	private Map<String, Stats> charStats = new HashMap<String, Stats>();
	private boolean needReport = false;
	private Thread senderThread;
	private final Object lock = new Object();

	private class Stats {
		public int base = 0;
		public int comp = 0;

		public Stats(int base, int comp) {
			this.base = base;
			this.comp = comp;
		}

		public void Update(int base, int comp) {
			this.base = base;
			this.comp = comp;
		}
	}

	private class Sender implements Runnable{
		private int SLEEP_TIME = 10*1000;
		private boolean fireRequest = false;

		public void run() {
			while (true) {
				String json = "";
				synchronized(lock) {
					if (needReport) {
						needReport = false;
						fireRequest = true;
						json = GenJson();
					}
				}
				if (fireRequest) {
					fireRequest = false;
					if (Config.statProto == "old") {
						// TODO: старый протокол, выпилить, когда будет реализация новой версии на сервере
						// http://domain.com/stats/char/secretKey/blood/ybile/phlegm/bbile
						String secretKey = Config.statKey;
						int blood = charStats.get("blood").base;
						int ybile = charStats.get("ybile").base;
						int phlegm = charStats.get("phlegm").base;
						int bbile = charStats.get("bbile").base;
						this.sendGet(secretKey, blood, ybile, phlegm, bbile);
					} else {
						this.sendPost(json);
					}
				}
				try {
					Thread.sleep(SLEEP_TIME);
				} catch (InterruptedException e) {
					return;
				}
			}
		}

		private void sendPost (String data) {
			try {
				URL url = new URL(Config.reportStatURL);
				URLConnection conn = (URLConnection)url.openConnection();
				conn.setDoOutput(true);
				conn.setDoInput(true);
				DataOutputStream wr = new DataOutputStream(conn.getOutputStream ());
				wr.writeBytes(data);
				wr.flush();
				wr.close();
				InputStream response = conn.getInputStream();
			} catch (Exception e) {
				return;
			}
		}

		private void sendGet (String secretKey, int blood, int ybile, int phlegm, int bbile) {
			try {
				// http://domain.com/stats/char/secretKey/blood/ybile/phlegm/bbile
				String urlString = Config.reportStatURL + '/' + secretKey + '/' + blood + '/' + ybile + '/' + phlegm + '/' + bbile;
				URL url = new URL(urlString);
				URLConnection conn = url.openConnection();
				InputStream response = conn.getInputStream();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}

	public StatReport () {
		Thread senderThread = new Thread(new Sender());
		senderThread.start();
	}

	public void UpdateStat (String name, int base, int comp) {
		this.charName = Config.currentCharName;

		synchronized(lock) {
			Stats stat = charStats.get(name);
			if (stat == null) {
				stat = new Stats(base, comp);
				charStats.put(name, stat);
				needReport = true;
			} else {
				if ( (stat.base != base || stat.comp != comp) ) {
					stat.Update(base, comp);
					needReport = true;
				}
			}
		}
	}

	public String GenJson () {
		try {
			JSONObject json = new JSONObject();

			json.put("loginname", this.loginName);
			json.put("charname", this.charName);

			JSONObject stats = new JSONObject();
			for (Map.Entry<String, Stats> entry : charStats.entrySet()) {
				String statName = entry.getKey();
				Stats statVals = entry.getValue();
				int base = statVals.base;
				int comp = statVals.comp;

				JSONObject stat = new JSONObject();
				stat.put("base", base);
				stat.put("comp", comp);

				stats.put(statName, stat);
			}

			json.put("stats", stats);
			return json.toString(2);
		} catch(Exception e) {
			return "";
		}
	}

	public void SetLoginName(String loginName) {
		synchronized(lock) {
			this.loginName = loginName;
		}
	}
}
