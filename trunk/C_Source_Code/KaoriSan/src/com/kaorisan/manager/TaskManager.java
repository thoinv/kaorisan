package com.kaorisan.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.kaorisan.beans.Attachment;
import com.kaorisan.beans.AttachmentTmp;
import com.kaorisan.beans.Audio;
import com.kaorisan.beans.Picture;
import com.kaorisan.beans.RecommendTask;
import com.kaorisan.beans.Reply;
import com.kaorisan.beans.Task;
import com.kaorisan.common.DebugLog;
import com.kaorisan.common.Utils;
import com.kaorisan.dataLayer.CacheData;
import com.kaorisan.service.NetworkService;

public class TaskManager {
	public int numberOfUploadSucess = 0;
	public int numberOfUploadFail = 0;
	public int totalNumber = 0;

	public interface OnPostPushToken {
		void onPostPushTokenMethod(boolean isSuccess, String message);
	}

	OnPostPushToken callbackOnPostPushToken = null;

	public interface OnGetTaskRecommend {
		void onGetTaskRecommend(boolean isSuccess, ArrayList<RecommendTask> listRecommendTask, String message);
	}

	OnGetTaskRecommend callbackOnGetTaskRecommend = null;

	public interface OnGetListTaskResult {
		void onGetListTaskResultMethod(boolean isSuccess, List<Task> listTask, String message);
	}

	OnGetListTaskResult callbackOnGetTaskResult = null;

	public interface OnUploadPhotoToTaskResult {
		void onUploadPhotoToTaskMethod(boolean isSuccess, String message);
	}

	OnUploadPhotoToTaskResult callbackOnUploadPhotoToTaskResult = null;

	public interface OnGetRequestResult {
		void onGetRequestResultMethod(boolean isSuccess, Task task, String message);
	}

	OnGetRequestResult callbackOnGetRequestResult = null;

	public interface OnGetRateTaskResult {
		void onGetRateTaskMethod(boolean isSuccess, String message);
	}

	OnGetRateTaskResult callbackOnGetRateTaskResult = null;

	public interface OnCreateNewTaskResult {
		void onCreateNewTaskMethod(boolean isSuccess, Task task, String message);
	}

	OnCreateNewTaskResult callbackOnCreateNewTaskResult = null;

	public interface OnCreateNewRepLyResult {
		void onCreateNewRepLyMethod(boolean isSuccess, String message);
	}

	public void getTaskRecommend(final String token, final String count,
			OnGetTaskRecommend onGetTaskRecommend) {
		callbackOnGetTaskRecommend = onGetTaskRecommend;

		final NetworkService service = new NetworkService();

		new AsyncTask<String, Integer, ArrayList<RecommendTask>>() {

			@Override
			protected ArrayList<RecommendTask> doInBackground(String... params) {
				RecommendTask recommendTask = null;
				ArrayList<RecommendTask> listRecommendTask = new ArrayList<RecommendTask>();
				try {
					JSONArray jsonResult = service.getRecommendTasks(token, count);
					JSONObject tmpObject = null;
					
					for (int i = 0; i < jsonResult.length(); i++) {
						recommendTask = new RecommendTask();
						tmpObject = jsonResult.getJSONObject(i);

						if (tmpObject.has("title")) {
							recommendTask.setTitle(tmpObject.getString("title"));
						}
						if (tmpObject.has("request")) {
							recommendTask.setRequestContent(tmpObject.getString("request"));
						}

						listRecommendTask.add(recommendTask);
					}
					
					return listRecommendTask;
				} catch (Exception exception) {
					DebugLog.loge("Get recommend task exeption: " + exception.getMessage());
				}
				return null;
			}

			@Override
			protected void onPostExecute(
					ArrayList<RecommendTask> listRecommendTask) {
				super.onPostExecute(listRecommendTask);
				try {
					if (listRecommendTask != null) {
						callbackOnGetTaskRecommend.onGetTaskRecommend(true, listRecommendTask, null);
					} else {
						callbackOnGetTaskRecommend.onGetTaskRecommend(false, new ArrayList<RecommendTask>(), "Network is unreachable");
					}
				} catch (Exception exception) {
					DebugLog.loge("Get recommend task exception: " + exception.getMessage());
				}
			};
		}.execute(token, count);

	}

	public void postPushToken(String token, String platform, String pushToken, OnPostPushToken onPostPushToken) {
		
		callbackOnPostPushToken = onPostPushToken;

		final NetworkService service = new NetworkService();

		new AsyncTask<String, String, JSONObject>() {

			@Override
			protected JSONObject doInBackground(String... params) {
				DebugLog.logd(params[0] + " " + params[1] + " " + params[2]);
				return service.pushToken(params[0], params[1], params[2]);
			}

			@Override
			protected void onPostExecute(JSONObject result) {
				super.onPostExecute(result);
				if (result != null) {
					if (!result.has("message")) {
						try {
							DebugLog.logd("Status: " + result.getString("status"));
							callbackOnPostPushToken.onPostPushTokenMethod(true, null);
						} catch (JSONException e) {
							DebugLog.loge("Post pushToken error: " + e.getMessage());
						}
						
					} else {
						callbackOnPostPushToken.onPostPushTokenMethod(false, null);
					}
				} else {
					DebugLog.logd("result is null");
					callbackOnPostPushToken.onPostPushTokenMethod(false, null);
				}

			}
		}.execute(token, platform, pushToken);
	}

	OnCreateNewRepLyResult callbackOnCreateNewRepLyResult = null;

	public void getListTask(String token, String page, String count, final OnGetListTaskResult onGetListTaskResult) {
		
		callbackOnGetTaskResult = onGetListTaskResult;
		final NetworkService service = new NetworkService();
		new AsyncTask<String, String, ArrayList<Task>>() {

			@Override
			protected ArrayList<Task> doInBackground(String... params) {
				JSONArray jsonResult = service.getListTask(params[0], params[1], params[2]);
				JSONObject tmpObject;
				Task task = null;
				ArrayList<Task> listTask = new ArrayList<Task>();
				try {
					for (int i = 0; i < jsonResult.length(); i++) {
						task = new Task();
						tmpObject = jsonResult.getJSONObject(i);
						Log.i("ID", "" + tmpObject.getInt("id"));
						if (tmpObject.has("id")) {
							task.setId(tmpObject.getInt("id"));
						}
						
						if (tmpObject.has("title")) {
							task.setTitle(tmpObject.getString("title"));
						}
						
						if (tmpObject.has("workflow_state")) {
							task.setWorkflowState(tmpObject.getString("workflow_state"));
						}
						
						if (tmpObject.has("replies_count")) {
							task.setRepliesCount(tmpObject.getInt("replies_count"));
						}
						
						if (tmpObject.has("created_at")) {
							task.setCreatedAt(tmpObject.getString("created_at"));
						}
						
						if (tmpObject.has("updated_at")) {
							task.setUpdateAt(tmpObject.getString("updated_at"));
						}
						
						if (tmpObject.has("completed_at")) {
							task.setCompletedAt(tmpObject.getString("completed_at"));
						}
						listTask.add(task);
						
					}
					return listTask;
				} catch (Exception e) {
					DebugLog.logd(e.getMessage());
				}

				return null;
			}

			protected void onPostExecute(ArrayList<Task> listTask) {
				if (listTask != null) {
					callbackOnGetTaskResult.onGetListTaskResultMethod(true, listTask, null);
				} else {
					callbackOnGetTaskResult.onGetListTaskResultMethod(false, new ArrayList<Task>(), "Network is unreachable");
				}
			}
		}.execute(token, page, count);
	}

	public void uploadPhotoToTask(String token, String imagePath, String id, 
			final AttachmentTmp attachmentTmp, OnUploadPhotoToTaskResult onUploadPhotoToTaskResult) {
		
		final NetworkService service = new NetworkService();
		callbackOnUploadPhotoToTaskResult = onUploadPhotoToTaskResult;
		new AsyncTask<String, String, JSONObject>() {

			@Override
			protected JSONObject doInBackground(String... params) {
				return service.uploadPhotoToTask(params[0], new File(params[1]), params[2]);
			}

			protected void onPostExecute(JSONObject result) {
				// if(result != null){
				// DebugLog.logd("Result upload " + result.toString());
				// callbackOnUploadPhotoToTaskResult.onUploadPhotoToTaskMethod(true);
				// }
				// callbackOnUploadPhotoToTaskResult.onUploadPhotoToTaskMethod(false);
				if (result == null) {
					DebugLog.logd("Result return null");
					if (CacheData.getInstant().getCurrentProgressDialog() != null) {
						CacheData.getInstant().getCurrentProgressDialog().dismiss();
						CacheData.getInstant().setCurrentProgressDialog(null);
					}

					numberOfUploadFail++;
					callbackOnUploadPhotoToTaskResult.onUploadPhotoToTaskMethod(false, "Upload failed!");
				} else {
					if (result.has("status")) {
						DebugLog.logd("Result upload " + result.toString());
						numberOfUploadFail++;
						callbackOnUploadPhotoToTaskResult.onUploadPhotoToTaskMethod(false, null);
					} else {
						numberOfUploadSucess++;
						// Picture picture = new Picture();
						// picture.setFileName(attachment.getFileName());
						// picture.setFilePath(attachment.getFilePath());
						// CacheData.getInstant().getCurrentTask().getLiPictures()
						// .add(picture);
						for (int i = CacheData.getInstant().getListAttachment().size() - 1; i >= 0; i--) {
							if (CacheData.getInstant().getListAttachment().get(i).getFilePath().equals(attachmentTmp.getFilePath())) {
								CacheData.getInstant().getListAttachment().get(i).setIsLocal(2);
								break;
							}
						}
						if(attachmentTmp.isCamera() == false && attachmentTmp.getIsLocal() == 1 && attachmentTmp.isResize() == true){
							Utils.deleteFile(attachmentTmp.getFilePath());
						}
						CacheData.getInstant().getListAttachmentTmps().remove(attachmentTmp);
						callbackOnUploadPhotoToTaskResult.onUploadPhotoToTaskMethod(true, null);
						DebugLog.logd("Result upload " + result.toString());
					}
				}
			}

		}.execute(token, imagePath, id);
	}

	public void uploadAudioToTask(String token, String audioPath, String id, final Attachment attachment,
			OnUploadPhotoToTaskResult onUploadPhotoToTaskResult) {
		
		final NetworkService service = new NetworkService();
		callbackOnUploadPhotoToTaskResult = onUploadPhotoToTaskResult;
		new AsyncTask<String, String, JSONObject>() {

			@Override
			protected JSONObject doInBackground(String... params) {
				return service.uploadAudioToTask(params[0], params[1],
						new File(params[2]));
			}

			protected void onPostExecute(JSONObject result) {
				if (result != null) {
					if (result.has("status")) {
						callbackOnUploadPhotoToTaskResult.onUploadPhotoToTaskMethod(false, null);
					} else {

						callbackOnUploadPhotoToTaskResult.onUploadPhotoToTaskMethod(true, null);
						DebugLog.logd("Result upload " + result.toString());
					}
				}
			}
		}.execute(token, id, audioPath);
	}

	public void getTaskDetail(String id, String token, OnGetRequestResult onGetRequestResult) {
		callbackOnGetRequestResult = onGetRequestResult;
		final NetworkService service = new NetworkService();

		new AsyncTask<String, String, Task>() {

			@Override
			protected Task doInBackground(String... params) {
				JSONObject jsonResult = service.getTaskDetail(params[0], params[1]);
				JSONObject tmpObject;
				Task task;
				ArrayList<Reply> listReply = new ArrayList<Reply>();
				ArrayList<Picture> listPictures = new ArrayList<Picture>();
				ArrayList<Audio> listAudios = new ArrayList<Audio>();
				ArrayList<Attachment> listAttachment = new ArrayList<Attachment>();
				Attachment attachment;
				Reply reply;
				Picture picture;
				Audio audio;
				try {
					if (jsonResult != null) {
						if (!jsonResult.has("status")) {
							task = new Task();
							if (jsonResult.has("id")) {
								task.setId(jsonResult.getInt("id"));
							}
							if (jsonResult.has("title")) {
								task.setTitle(jsonResult.getString("title"));
							}
							if (jsonResult.has("request")) {
								task.setRequest(jsonResult.getString("request"));
							}
							if (jsonResult.has("replies_count")) {
								task.setRepliesCount(jsonResult.getInt("replies_count"));
							}
							if (jsonResult.has("workflow_state")) {
								task.setWorkflowState(jsonResult.getString("workflow_state"));
							}
							if (jsonResult.has("created_at")) {
								task.setCreatedAt(jsonResult.getString("created_at"));
							}
							if (jsonResult.has("completed_at")) {
								task.setCompletedAt(jsonResult.getString("completed_at"));
							}

							if (jsonResult.has("rateable")) {
								task.setRated(jsonResult.getBoolean("rateable"));
							}
							JSONArray jsonListReplies = jsonResult.getJSONArray("replies");
							for (int i = 0; i < jsonListReplies.length(); i++) {
								reply = new Reply();
								tmpObject = jsonListReplies.getJSONObject(i);
								if (tmpObject.has("id")) {
									reply.setId(tmpObject.getInt("id"));
								}
								if (tmpObject.has("body")) {
									reply.setBody(tmpObject.getString("body"));
									// ArrayList<String> listString = new
									// ArrayList<String>();
									// listString.add(tmpObject.getString("body"));
									// reply.setArrayChildren(listString);
								}
								if (tmpObject.has("created_at")) {
									reply.setCreatedAt(tmpObject.getString("created_at"));
								}
								if (tmpObject.has("is_assistant")) {
									reply.setAssistant(tmpObject.getBoolean("is_assistant"));
								}
								listReply.add(reply);
							}
							task.setListReply(listReply);
							task.setLiPictures(listPictures);
							JSONArray jsonListAudios = jsonResult.getJSONArray("audios");
							
							for (int i = 0; i < jsonListAudios.length(); i++) {
								audio = new Audio();
								attachment = new Attachment();
								attachment.setTmp(0);
								attachment.setType("audio");
								attachment.setCamera(false);
								attachment.setResize(false);
								tmpObject = jsonListAudios.getJSONObject(i);
								if (tmpObject.has("id")) {
									audio.setId(tmpObject.getInt("id"));
									attachment.setId(tmpObject.getInt("id"));
								}
								if (tmpObject.has("url")) {
									audio.setFilePath(tmpObject.getString("url"));
									audio.setFileName(tmpObject.getString("url").substring(tmpObject.getString("url").lastIndexOf('/') + 1, 
											tmpObject.getString("url").length()));
									attachment.setFilePath(tmpObject.getString("url"));
									attachment.setFileName(tmpObject.getString("url").substring(tmpObject.getString("url").lastIndexOf('/') + 1,
											tmpObject.getString("url").length()));
								}
								if (tmpObject.has("created_at")) {
									audio.setCreateAt(tmpObject.getString("created_at"));
									attachment.setCreateAt(tmpObject.getString("created_at"));
								}
								listAudios.add(audio);
								listAttachment.add(attachment);
							}
							task.setListAudios(listAudios);

							JSONArray jsonListPictures = jsonResult.getJSONArray("pictures");
							for (int i = 0; i < jsonListPictures.length(); i++) {
								picture = new Picture();
								attachment = new Attachment();
								attachment.setType("image");
								attachment.setCamera(false);
								attachment.setResize(false);
								tmpObject = jsonListPictures.getJSONObject(i);
								if (tmpObject.has("id")) {
									picture.setId(tmpObject.getInt("id"));
									attachment.setId(tmpObject.getInt("id"));
								}
								if (tmpObject.has("url")) {
									picture.setFilePath(tmpObject.getString("url"));
									picture.setFileName(tmpObject.getString("url").substring(tmpObject.getString("url").lastIndexOf('/') + 1,
													tmpObject.getString("url").length()));

									attachment.setFilePath(tmpObject.getString("url"));
									attachment.setFileName(tmpObject.getString("url").substring(tmpObject.getString("url")
																	.lastIndexOf('/') + 1, tmpObject.getString("url").length()));
								}
								if (tmpObject.has("created_at")) {
									picture.setCreateAt(tmpObject.getString("created_at"));
									attachment.setCreateAt(tmpObject.getString("created_at"));
								}
								listPictures.add(picture);
								listAttachment.add(attachment);
							}
							CacheData.getInstant().setListAttachment(listAttachment);
							// task.setListAttachments(listAttachment);
							return task;
						} else {
							return null;
						}
					}
					return null;
				} catch (JSONException e) {
					DebugLog.logd(e.getMessage());
				}
				return null;
			}

			protected void onPostExecute(Task task) {
				if (task != null) {
					callbackOnGetRequestResult.onGetRequestResultMethod(true, task, null);
				} else {
					callbackOnGetRequestResult.onGetRequestResultMethod(false, new Task(), "Server crashed");
				}
			}
		}.execute(id, token);
	}

	public void rateTask(String token, String id, String rate, OnGetRateTaskResult onGetRateTaskResult) {
		callbackOnGetRateTaskResult = onGetRateTaskResult;
		final NetworkService service = new NetworkService();

		new AsyncTask<String, String, JSONObject>() {

			@Override
			protected JSONObject doInBackground(String... params) {
				return service.rateTask(params[0], params[1], params[2]);

			}

			protected void onPostExecute(JSONObject result) {
				try {
					if (result == null) {
						callbackOnGetRateTaskResult.onGetRateTaskMethod(false, "Network is unreachable");
					} else {
						if (!result.has("message")) {
							callbackOnGetRateTaskResult.onGetRateTaskMethod(true, null);
						} else {
							String message = result.getString("message");
							callbackOnGetRateTaskResult.onGetRateTaskMethod(false, message);
						}
					}
				} catch (JSONException e) {
					DebugLog.logd(e.getMessage());
				}
			}
		}.execute(token, id, rate);
	}

	public void createNewTask(String token, String title, String request,String isVip,OnCreateNewTaskResult onCreateNewTaskResult) {
		callbackOnCreateNewTaskResult = onCreateNewTaskResult;
		final NetworkService service = new NetworkService();

		new AsyncTask<String, String, Task>() {

			@Override
			protected Task doInBackground(String... params) {
				JSONObject jsonResult = service.createTask(params[0], params[1], params[2],params[3]);
				Task task;
				try {
					if (jsonResult != null) {
						if (!jsonResult.has("status")) {
							task = new Task();
							if (jsonResult.has("id")) {
								task.setId(jsonResult.getInt("id"));
							}
							if (jsonResult.has("title")) {
								task.setTitle(jsonResult.getString("title"));
							}
							if (jsonResult.has("request")) {
								task.setRequest(jsonResult.getString("request"));
							}
							if (jsonResult.has("replies_count")) {
								task.setRepliesCount(jsonResult.getInt("replies_count"));
							}
							if (jsonResult.has("workflow_state")) {
								task.setWorkflowState(jsonResult.getString("workflow_state"));
							}
							if (jsonResult.has("created_at")) {
								task.setCreatedAt(jsonResult.getString("created_at"));
							}
							return task;
						} else {
							return null;
						}
					} else {
						return null;
					}
				} catch (JSONException e) {
					DebugLog.logd(e.getMessage());
				}
				return null;
			}

			protected void onPostExecute(Task task) {
				if (task != null) {
					callbackOnCreateNewTaskResult.onCreateNewTaskMethod(true, task, null);
				} else {
					callbackOnCreateNewTaskResult.onCreateNewTaskMethod(false, null, null);
				}
			}
		}.execute(token, title, request,isVip);
	}

	public void creatNewRepLy(String token, String body, String id, OnCreateNewRepLyResult onCreateNewRepLyResult) {
		callbackOnCreateNewRepLyResult = onCreateNewRepLyResult;
		final NetworkService service = new NetworkService();
		new AsyncTask<String, String, JSONObject>() {

			@Override
			protected JSONObject doInBackground(String... params) {
				return service.createNewRepLy(params[0], params[1], params[2]);
			}

			protected void onPostExecute(JSONObject result) {
				try {
					if (result != null) {
						if (result.has("status")) {
							callbackOnCreateNewRepLyResult.onCreateNewRepLyMethod(false, result.getString("message"));
						} else {
							callbackOnCreateNewRepLyResult.onCreateNewRepLyMethod(true, null);
						}
					} else {
						callbackOnCreateNewRepLyResult.onCreateNewRepLyMethod(false, "Network is unreachable");
					}
				} catch (JSONException e) {
					DebugLog.logd(e.getMessage());
				}
			}
		}.execute(token, body, id);
	}
}
