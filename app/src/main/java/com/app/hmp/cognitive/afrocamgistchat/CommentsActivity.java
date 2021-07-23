package com.app.hmp.cognitive.afrocamgistchat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.app.hmp.cognitive.afrocamgistchat.activity.BaseActivity;
import com.app.hmp.cognitive.afrocamgistchat.activity.Comment;
import com.app.hmp.cognitive.afrocamgistchat.activity.CommentAdapter;
import com.app.hmp.cognitive.afrocamgistchat.activity.LocalStorage;
import com.app.hmp.cognitive.afrocamgistchat.activity.Logger;
import com.app.hmp.cognitive.afrocamgistchat.activity.UrlEndpoints;
import com.app.hmp.cognitive.afrocamgistchat.activity.Utils;
import com.app.hmp.cognitive.afrocamgistchat.api.Webservices;
import com.app.hmp.cognitive.afrocamgistchat.constants.Constants;
import com.google.gson.GsonBuilder;
import com.kaopiz.kprogresshud.KProgressHUD;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.app.hmp.cognitive.afrocamgistchat.activity.CommentAdapter.*;

public class CommentsActivity extends BaseActivity implements OnCommentClickListener {

    public static ArrayList<Comment> comments = new ArrayList<>();
    private int postId = 0;
    private EditText userComment;
    private CommentAdapter adapter;
    private KProgressHUD hud;
    private TextView replyTo;
    private int parentPosition = -1;
    private Comment parentComment;
    private int subCommentPosition = -1;
    private Comment subComment;
    RecyclerView commentList;
    private boolean isEditComment = false;
    public static boolean isComment = true,isCommentPosted = false;
    private boolean isApiCall = false;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        if (getIntent().getSerializableExtra("comments")!=null) {
            comments = (ArrayList<Comment>)  getIntent().getSerializableExtra("comments");
        }

        postId = getIntent().getIntExtra("postId",0);

        initView();
        initClickListener();
        initTextChangeListener();
        setRecyclerView();
    }

    private void initView() {

        userComment = findViewById(R.id.user_comment);
        replyTo = findViewById(R.id.reply_to);
    }

    private void initClickListener() {

        findViewById(R.id.comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utils.isConnected()) {
                    if (!userComment.getText().toString().equals("")) {
                       if(!isApiCall) {
                           if (isEditComment)
                               editComment();
                           else if (parentPosition == -1)
                               postComment();
                           else {
                               replyToComment();
                           }
                       }
                    }
                } else {
                    Utils.showAlert(CommentsActivity.this, getString(R.string.no_internet_connection_available));
                }
            }
        });

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.replying_layout).setVisibility(View.GONE);
                userComment.setText("");
                parentPosition = -1;
            }
        });

        findViewById(R.id.back).setOnClickListener(v -> {
            onBackPressed();
        });
    }

    //set comments listing view
    private void setRecyclerView() {

        if (comments!=null && comments.size() > 0) {
            commentList = findViewById(R.id.comments);
            commentList.setLayoutManager(new LinearLayoutManager(this));
            adapter = new CommentAdapter(this, comments, this);
            commentList.setAdapter(adapter);

            findViewById(R.id.comments).setVisibility(View.VISIBLE);
            findViewById(R.id.no_comments).setVisibility(View.GONE);

        } else {

            findViewById(R.id.comments).setVisibility(View.GONE);
            findViewById(R.id.no_comments).setVisibility(View.VISIBLE);
        }
    }

    //add comment to post API
    private void postComment() {

        JSONObject request = new JSONObject();
        try {
            request.put("post_id",postId);
            request.put("comment_text",userComment.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        isApiCall = true;
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.AUTHORIZATION, "Bearer " + LocalStorage.getLoginToken());

        Webservices.getData(Webservices.Method.POST, request,headers, UrlEndpoints.COMMENTS, response -> {
            isApiCall = false;
            try {

                JSONObject object = new JSONObject(response);

                if (object.has(Constants.MESSAGE)) {
                    Utils.showAlert(CommentsActivity.this, object.getString(Constants.MESSAGE));
                } else {
                    isCommentPosted = true;
                    addComment(object.getJSONObject("data").getInt("comment_id"));
                }

            } catch (Exception e) {
                Utils.showAlert(CommentsActivity.this, getString(R.string.opps_something_went_wrong));
                e.printStackTrace();
            }
        });
    }

    //display new comment on view
    private void addComment(int commentId) {

        Comment comment = new Comment();

        comment.setCommentId(commentId);
        comment.setCommentText(userComment.getText().toString());
        comment.setCommentedBy(LocalStorage.getUserDetails());
        comment.setLiked(false);

        comments.add(comment);

        setRecyclerView();
        commentList.scrollToPosition(comments.size() - 1);

        /*if (adapter==null){
            setRecyclerView();
        } else {
            adapter.notifyDataSetChanged();
            commentList.scrollToPosition(comments.size() - 1);
        }*/

        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(CommentsActivity.this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        findViewById(R.id.comments).setVisibility(View.VISIBLE);
        findViewById(R.id.no_comments).setVisibility(View.GONE);

        userComment.setText("");
    }

    //reply to comment API call
    private void replyToComment() {

        JSONObject request = new JSONObject();
        try {
            request.put("post_id",postId);
            request.put("comment_text",userComment.getText().toString());
            request.put("comment_parent_id",parentComment.getCommentId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(CommentsActivity.this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        isApiCall = true;
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.AUTHORIZATION, "Bearer " + LocalStorage.getLoginToken());

        Webservices.getData(Webservices.Method.POST, request,headers, UrlEndpoints.COMMENTS, response -> {
            try {
                isApiCall = false;
                JSONObject object = new JSONObject(response);

                if (object.has(Constants.MESSAGE)) {
                    Utils.showAlert(CommentsActivity.this, object.getString(Constants.MESSAGE));
                } else {
                    isCommentPosted = true;
                    addReply(object.getJSONObject("data").getInt("comment_id"));
                }

            } catch (Exception e) {
                Utils.showAlert(CommentsActivity.this, getString(R.string.opps_something_went_wrong));
                e.printStackTrace();
            }
        });
    }

    //display reply on listing view
    private void addReply(int commentId) {

        Comment comment = new Comment();

        comment.setCommentId(commentId);
        comment.setCommentText(userComment.getText().toString());
        comment.setCommentedBy(LocalStorage.getUserDetails());
        comment.setLiked(false);

        ArrayList<Comment> subComments = new ArrayList<>();

        if (parentComment.getSubComments()!=null)
            subComments = parentComment.getSubComments();

        subComments.add(comment);
        parentComment.setSubComments(subComments);

        comments.set(parentPosition, parentComment);
        commentList.scrollToPosition(parentPosition);

        adapter.notifyDataSetChanged();

        parentPosition = -1;

        userComment.setText("");
    }

    //edit comment API call
    private void editComment() {

        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f)
                .setCancellable(false)
                .show();

        JSONObject request = new JSONObject();
        try {
            request.put("comment_text",userComment.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        isApiCall = true;
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.AUTHORIZATION, "Bearer " + LocalStorage.getLoginToken());

        String url = UrlEndpoints.COMMENTS + "/" + (subComment==null ? parentComment.getCommentId() : subComment.getCommentId());

        Webservices.getData(Webservices.Method.PUT, request,headers, url, response -> {
            hud.dismiss();
            isApiCall = false;
            try {

                JSONObject object = new JSONObject(response);

                if (object.has(Constants.MESSAGE)) {
                    Utils.showAlert(CommentsActivity.this, object.getString(Constants.MESSAGE));
                } else {
                    isCommentPosted = true;
                    updateEditedComment();
                }

            } catch (Exception e) {
                Logger.d("executed111","yes...");
                //Utils.showAlert(CommentsActivity.this, "Oops something went wrong....");
                e.printStackTrace();
            }
        });
    }

    //update view after comment edit
    private void updateEditedComment() {

        if (subComment==null) {
            parentComment.setCommentText(userComment.getText().toString());
        } else {
            parentComment.getSubComments().get(subCommentPosition).setCommentText(userComment.getText().toString());
        }
        comments.set(parentPosition,parentComment);

        parentPosition = -1;
        isEditComment = false;
        adapter.notifyDataSetChanged();

        userComment.setText("");
    }

    @Override
    public void onLikeCommentClicked(Comment comment) {

        Logger.d("clickedItem", new GsonBuilder().setPrettyPrinting().create().toJson(comment));

        likeComment(comment.getCommentId());

    }

    @Override
    public void onReplyClicked(int position,Comment parentComment, Comment comment) {

        String replyingTo = "Replying to ";
        parentPosition = position;
        this.parentComment = parentComment;

        if (comment==null) {
            String reply = "@" + parentComment.getCommentedBy().getFirstName() + " " + parentComment.getCommentedBy().getLastName() + " -";
            replyingTo += parentComment.getCommentedBy().getFirstName() + " " + parentComment.getCommentedBy().getLastName();
            userComment.setText(reply);
            userComment.setSelection(reply.length());
        } else {
            String reply = "@" + comment.getCommentedBy().getFirstName() + " " + comment.getCommentedBy().getLastName() + " -";
            replyingTo += comment.getCommentedBy().getFirstName() + " " + comment.getCommentedBy().getLastName();
            userComment.setText(reply);
            userComment.setSelection(reply.length());
        }

        findViewById(R.id.replying_layout).setVisibility(View.VISIBLE);
        replyTo.setText(replyingTo);
    }

    @Override
    public void onDeleteCommentClicked(int parentPosition, Comment parentComment, int position, Comment comment) {

        //Logger.d("CommentId99","="+comment.getCommentId());
        Logger.d("CommentId99","=="+parentComment.getCommentId());

        if(Utils.isConnected())
            deleteComment(parentPosition,parentComment,position,comment);
        else
            Utils.showAlert(CommentsActivity.this, getString(R.string.no_internet_connection_available));
    }

    //delete comment API call
    private void deleteComment(int parentPosition, Comment parentComment, int position, Comment comment) {

        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f)
                .setCancellable(true)
                .show();

        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.AUTHORIZATION, "Bearer " + LocalStorage.getLoginToken());

        String url = UrlEndpoints.COMMENTS + "/" + (comment==null ? parentComment.getCommentId() : comment.getCommentId());

        Webservices.getData(Webservices.Method.DELETE, new ArrayList<>(),headers, url, response -> {
            hud.dismiss();
            try {

                JSONObject object = new JSONObject(response);

                if (object.has(Constants.MESSAGE)) {
                    Utils.showAlert(CommentsActivity.this, object.getString(Constants.MESSAGE));
                } else {
                    isCommentPosted = true;
                    removeComment(parentPosition, parentComment, position, comment);
                }

            } catch (Exception e) {
                Utils.showAlert(CommentsActivity.this, getString(R.string.opps_something_went_wrong));
                e.printStackTrace();
            }
        });
    }

    //remove deleted comment from view
    private void removeComment(int parentPosition, Comment parentComment, int position, Comment comment) {

        if (comment == null) {
            comments.remove(parentPosition);
            adapter.notifyDataSetChanged();

            if (comments.size() == 0) {
                findViewById(R.id.comments).setVisibility(View.GONE);
                findViewById(R.id.no_comments).setVisibility(View.VISIBLE);
            }

        } else {

            ArrayList<Comment> subComments = parentComment.getSubComments();
            subComments.remove(position);

            parentComment.setSubComments(subComments);

            comments.set(parentPosition,parentComment);

            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onEditCommentClicked(int parentPosition, Comment parentComment, int position, Comment comment) {

        this.parentPosition = parentPosition;
        this.parentComment = parentComment;
        subCommentPosition = position;
        subComment = comment;
        isEditComment = true;

        if (comment==null) {
            userComment.setText(parentComment.getCommentText());
            userComment.setSelection(parentComment.getCommentText().length());
        } else {
            userComment.setText(comment.getCommentText());
            userComment.setSelection(comment.getCommentText().length());
        }

    }

    private void initTextChangeListener() {

        userComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ("".equals(userComment.getText().toString())) {
                    findViewById(R.id.replying_layout).setVisibility(View.GONE);
                    parentPosition = -1;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //like comment API call
    private void likeComment(int commentId) {

        JSONObject request = new JSONObject();
        try {
            request.put("comment_id",commentId);
            request.put("like_type","comment");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Logger.d("comment_id",""+commentId);
        Logger.d("Token1", LocalStorage.getLoginToken());

        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.AUTHORIZATION, "Bearer " + LocalStorage.getLoginToken());

        Webservices.getData(Webservices.Method.POST, request,headers, UrlEndpoints.LIKE_UNLIKE, response -> {
            try {

                JSONObject object = new JSONObject(response);

                if (object.has(Constants.MESSAGE)) {
                    Utils.showAlert(CommentsActivity.this, object.getString(Constants.MESSAGE));
                } else {

                    Logger.d("commentLike",new GsonBuilder().setPrettyPrinting().create().toJson(object));


                    isCommentPosted = true;
                }

            } catch (Exception e) {
                Utils.showAlert(CommentsActivity.this, getString(R.string.opps_something_went_wrong));
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isCommentPosted){
            isComment = true;

            Intent intent = new Intent();
            intent.putExtra("comments",comments);
            setResult(RESULT_OK,intent);
            finish();
        } else {
            isComment = true;
            super.onBackPressed();
        }
    }
}
