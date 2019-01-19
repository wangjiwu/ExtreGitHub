package com.example.lenovo.httpapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class RepoDetailActivity extends AppCompatActivity {

    TextView repo_intro,repo_time, repo_name, owner_text,update_text,
            language_text, authority_text, capacity_text  ;
    ImageView personImages;
    ImageView  go_back,share;
    //ProgressBar loddingbar3;
    Button events,issues,stargazers,contributors,forks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_detail);

        repo_intro = findViewById(R.id.repo_intro);
        repo_time = findViewById(R.id.repo_time);
        repo_name = findViewById(R.id.repo_name);
        owner_text = findViewById(R.id.owner_text);
        update_text = findViewById(R.id.update_text);
        language_text = findViewById(R.id.language_text);
        authority_text = findViewById(R.id.authority_text);
        capacity_text = findViewById(R.id.capacity_text);
        personImages = findViewById(R.id.personImages);
        go_back = findViewById(R.id.go_back);
        //loddingbar3 = findViewById(R.id.loddingbar3);
        events = findViewById(R.id.events);
        issues = findViewById(R.id.issues);
        stargazers = findViewById(R.id.stargazers);
        contributors = findViewById(R.id.contributors);
        forks = findViewById(R.id.forks);
        share =  findViewById(R.id.share);
        final Repo detailRepo = (Repo) getIntent().getExtras().getSerializable("repo");



        repo_intro.setText(detailRepo.getDescription());
        repo_time.setText(detailRepo.getCreated_at());




        if (detailRepo.getName().length() < 10) {
            repo_name.setText(detailRepo.getName());
        } else {
            repo_name.setText( detailRepo.getName().substring(0,10) + "...");
        }

        update_text.setText(detailRepo.getUpdated_at());
        owner_text.setText(detailRepo.getOwner().getLogin());
        language_text.setText(detailRepo.getLanguage());
        capacity_text.setText(detailRepo.getSize() + "Kb");


         Picasso.with(RepoDetailActivity.this).load(detailRepo.getOwner().getAvatar_url())
                .noFade()
                .into( personImages);
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(RepoDetailActivity.this,RepoEventActivity.class);
                intent.putExtra("userName",detailRepo.getOwner().getLogin() );
                intent.putExtra("repoName",detailRepo.getName());
                intent.putExtra("flag","fromRepo");
                startActivity(intent);
            }
        });
        issues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(RepoDetailActivity.this,RepoIssueActivity.class);
                intent.putExtra("userName",detailRepo.getOwner().getLogin() );
                intent.putExtra("repoName",detailRepo.getName());
                startActivity(intent);
            }
        });


        stargazers.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent intent = new Intent(RepoDetailActivity.this, RepoStargazerActivity.class);
                  intent.putExtra("userName",detailRepo.getOwner().getLogin() );
                  intent.putExtra("repoName",detailRepo.getName());
                  intent.putExtra("flag", "stargazers");
                  startActivity(intent);
              }
        });
        contributors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RepoDetailActivity.this, RepoStargazerActivity.class);
                intent.putExtra("userName",detailRepo.getOwner().getLogin() );
                intent.putExtra("repoName",detailRepo.getName());
                intent.putExtra("flag", "contributors");
                startActivity(intent);


            }
        });

        forks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(RepoDetailActivity.this,RepoForksActivity.class);

                intent.putExtra("userName",detailRepo.getOwner().getLogin() );
                intent.putExtra("repoName",detailRepo.getName());
                intent.putExtra("flag", "fromRepoDetail");
                startActivity(intent);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                intent.putExtra(Intent.EXTRA_TEXT, "我找到了一个很好的Repo， 一起来看看吧！ 仓库地址：" + detailRepo.getHtml_url());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, getTitle()));
            }
        });






    }
}
