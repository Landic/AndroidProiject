package com.example.androidproject;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private final Random random =  new Random();
    private TextView tvScore;
    private TextView tvBestScore;
    private Animation spawnAnimation;
    private Animation collapseAnimation;
    private Animation bestScoreAnimation;
    private long score;
    private long bestScore;
    private final int N =4;
    private final int[][] tiles = new int[N][N];
    private final TextView[][] tvTiles = new TextView[N][N];

    @SuppressLint({"ClickableViewAccessibility", "DiscouragedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.game_layout_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        spawnAnimation = AnimationUtils.loadAnimation(this, R.anim.game_tile_spawn);
        collapseAnimation = AnimationUtils.loadAnimation(this, R.anim.game_tile_collapse);
        bestScoreAnimation = AnimationUtils.loadAnimation(this, R.anim.game_bestscore);


        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tvTiles[i][j]= findViewById(
                        getResources().getIdentifier(
                                "game_tile_"+ i+j,
                                "id",
                                getPackageName()
                        )
                );
            }

        }
        tvScore =findViewById(R.id.game_tv_score);
        tvBestScore =findViewById(R.id.game_tv_best);
        LinearLayout gameField = findViewById(R.id.game_layout_field);
        gameField.post(()->{
            int vw = this.getWindow().getDecorView().getWidth();
            int fieldMargin= 20;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    vw- 2* fieldMargin,
                    vw- 2* fieldMargin
            );
            layoutParams.setMargins(fieldMargin, fieldMargin, fieldMargin, fieldMargin);
            layoutParams.gravity = Gravity.CENTER;
            gameField.setLayoutParams(layoutParams);
        });
        gameField.setOnTouchListener(new OnSwipeListener(GameActivity.this){
            @Override
            public void onSwipeBottom() {
                Toast.makeText(GameActivity.this, "onSwipeBottom", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeLeft() {
                if(moveLeft()){
                    spawnTile();
                    updateField();
                }
                else{
                    Toast.makeText(GameActivity.this, "NO left move", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSwipeRight() {
                if(moveRight()){
                    spawnTile();
                    updateField();
                }
                else{
                    Toast.makeText(GameActivity.this, "NO right move", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSwipeTop() {
                Toast.makeText(GameActivity.this, "onSwipeTop", Toast.LENGTH_SHORT).show();
            }
        });

        bestScore = 10L;
        startNewGame();
    }

    private void startNewGame(){
        score = 0L;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                //tiles[i][j] = (int)Math.pow(2, i+j);
                tiles[i][j]= 0;
            }
        }
        spawnTile();
        spawnTile();
        updateField();
    }

    private boolean moveRight(){
        boolean res;
        res = shiftRight(false);
        for (int i = 0; i < N; i++) {
            for (int j = N-1; j >0; j--) {
                if(tiles[i][j] ==tiles[i][j-1] && tiles[i][j] !=0) {
                    tiles[i][j] *=2;
                    tiles[i][j-1] =0;
                    score +=tiles[i][j];
                    res =true;
                    tvTiles[i][j].setTag(collapseAnimation);
                }
            }
        }
        if(res){
            shiftRight(true);
        }
        return res;
    }
    private boolean moveLeft(){
        boolean res;
        res = shiftLeft(false);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j <N-1; j++) {
                if(tiles[i][j] ==tiles[i][j+1] && tiles[i][j] !=0) {
                    tiles[i][j] *=2;
                    tiles[i][j+1] =0;
                    score +=tiles[i][j];
                    res =true;
                    tvTiles[i][j].setTag(collapseAnimation);
                }
            }
        }
        if(res){
            shiftLeft(true);
        }
        return res;
    }
    private boolean shiftRight(Boolean shiftTags){
        boolean res = false;
        for (int i = 0; i < N; i++) {
            boolean wasReplace ;
            do{
                wasReplace= false;
                for (int j = 0; j < N-1; j++) {
                    if(tiles[i][j] != 0 && tiles[i][j+1] == 0){
                        tiles[i][j+1] = tiles[i][j];
                        tiles[i][j]=0;
                        wasReplace = true;
                        res = true;
                        if(shiftTags){
                            Object tag= tvTiles[i][j].getTag();
                            tvTiles[i][j].setTag(tvTiles[i][j+1].getTag() );
                            tvTiles[i][j+1].setTag(tag);
                        }
                    }
                }
            }while(wasReplace);
        }
        return res;
    }
    private boolean shiftLeft(Boolean shiftTags){
        boolean res = false;
        for (int i = 0; i < N; i++) {
            boolean wasReplace ;
            do{
                wasReplace= false;
                for (int j = 1; j < N; j++) {
                    if(tiles[i][j] != 0 && tiles[i][j - 1] == 0){
                        tiles[i][j - 1] = tiles[i][j];
                        tiles[i][j]=0;
                        wasReplace = true;
                        res = true;
                        if(shiftTags){
                            Object tag= tvTiles[i][j].getTag();
                            tvTiles[i][j].setTag(tvTiles[i][j - 1].getTag() );
                            tvTiles[i][j - 1].setTag(tag);
                        }
                    }
                }
            }while(wasReplace);
        }
        return res;
    }

    private boolean spawnTile(){
        boolean res = false;
        List<Integer> freeTiles =  new ArrayList<>(N* N);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if(tiles[i][j]==0){
                    freeTiles.add(N* i +j);
                }
            }
        }
        if(freeTiles.isEmpty()){
            return false;
        }
        int k = freeTiles.get(random.nextInt(freeTiles.size()));
        int i = k/N;
        int j = k%N;
        tiles[i][j]= random.nextInt(10)==0 ? 4 : 2;
        tvTiles[i][j].setTag( spawnAnimation);
        return res;
    }

    float getFontSize(int value) {
        if (value < 100) return 48.0f;
        if (value < 1000) return 42.0f;
        if (value < 10000) return 36.0f;
        return 28.0f;
    }

    @SuppressLint("DiscouragedApi")
    private void updateField(){
        tvScore.setText(getString(R.string.game_tv_score_tpl, scoreToString(score) ));
        tvBestScore.setText(getString(R.string.game_tv_best_tpl, scoreToString(bestScore) ));
        if (score > bestScore) {
            bestScore = score;
            tvBestScore.setTag(bestScoreAnimation);
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tvTiles[i][j].setText(scoreToString(tiles[i][j] ) );
                tvTiles[i][j].setTextColor(
                        getResources().getColor(
                                getResources().getIdentifier(
                                        String.format(Locale.ROOT,"game_tile%d_fg", tiles[i][j]),
                                        "color",
                                        getPackageName()
                                ),
                                getTheme()
                        )
                );

                tvTiles[i][j].getBackground().setColorFilter(
                        getResources().getColor(
                                getResources().getIdentifier(
                                        String.format(Locale.ROOT,"game_tile%d_bg", tiles[i][j]),
                                        "color",
                                        getPackageName()
                                ),
                                getTheme()
                        ),
                        PorterDuff.Mode.SRC_ATOP
                );
                tvTiles[i][j].setTextSize(getFontSize(tiles[i][j]));

                Object animTag = tvTiles[i][j].getTag();
                if(animTag instanceof Animation){
                    tvTiles[i][j].startAnimation((Animation) animTag);
                    tvTiles[i][j].setTag(null);
                }
                Object bestScoreTag = tvBestScore.getTag();
                if (bestScoreTag instanceof Animation) {
                    tvBestScore.startAnimation((Animation) bestScoreTag);
                    tvBestScore.setTag(null);
                }

            }
        }
    }

    private String scoreToString(long value){
        return String.valueOf(value);
    }
}