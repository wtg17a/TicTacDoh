package com.example.tictacdoh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer winSound;
    private MediaPlayer loseSound;

    private ImageButton[][] buttons = new ImageButton[3][3];

    private boolean player1Turn = true;

    private int roundCount;

    private int player1Points;
    private int player2Points;

    private TextView textViewPlayer1;
    private TextView textViewPlayer2;

    private Drawable nullIcon;
    private Drawable troyIcon;
    private Drawable timmoIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        winSound = MediaPlayer.create(MainActivity.this, R.raw.success);
        loseSound = MediaPlayer.create(MainActivity.this, R.raw.failure);

        textViewPlayer1 = findViewById(R.id.text_view_p1);
        textViewPlayer2 = findViewById(R.id.text_view_p2);

        nullIcon = getResources().getDrawable(R.drawable.null_icon);
        troyIcon = getResources().getDrawable(R.drawable.troy_icon);
        timmoIcon = getResources().getDrawable(R.drawable.timmo_icon);

        for(int i = 0; i < 3; ++i)
        {
            for(int j = 0; j < 3; ++j)
            {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
                buttons[i][j].setImageDrawable(nullIcon);
            }
        }

        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                resetGame();
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        if(!((ImageButton) v).getDrawable().equals(nullIcon))
        {
            return;
        }

        if(player1Turn)
        {
            ((ImageButton) v).setImageDrawable(troyIcon);
        }
        else
        {
            ((ImageButton) v).setImageDrawable(timmoIcon);
        }

        ++roundCount;

        if(checkForWin())
        {
            if(player1Turn)
            {
                player1Win();
                winSound.start();
            }
            else
            {
                player2Win();
                winSound.start();
            }
        }
        else if(roundCount == 9)
        {
            draw();
            loseSound.start();
        }
        else
        {
            player1Turn = !player1Turn;
        }
    }

    private boolean checkForWin()
    {
        Drawable[][] field = new Drawable[3][3];

        for(int i = 0; i < 3; ++i)
        {
            for(int j = 0; j < 3; ++j)
            {
                field[i][j] = buttons[i][j].getDrawable();
            }
        }

        for(int i = 0; i < 3; ++i)
        {
            if(field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals(nullIcon))
            {
                return true;
            }
        }

        for(int i = 0; i < 3; ++i)
        {
            if(field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals(nullIcon))
            {
                return true;
            }
        }

        if(field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals(nullIcon))
        {
            return true;
        }

        if(field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].equals(nullIcon))
        {
            return true;
        }

        return false;
    }

    private void player1Win()
    {
        ++player1Points;
        Toast.makeText(this, "Player 1 Wins!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }

    private void player2Win()
    {
        ++player2Points;
        Toast.makeText(this, "Player 2 Wins!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }

    private void draw()
    {
        Toast.makeText(this, "Not again...", Toast.LENGTH_SHORT).show();
        resetBoard();
    }

    private void updatePointsText()
    {
        textViewPlayer1.setText("Player 1: " + player1Points);
        textViewPlayer2.setText("Player 2: " + player2Points);
    }

    private void resetBoard()
    {
        for(int i = 0; i < 3; ++i)
        {
            for(int j = 0; j < 3; ++j)
            {
                buttons[i][j].setImageDrawable(nullIcon);
            }
        }

        roundCount = 0;
        player1Turn = true;
    }

    private void resetGame()
    {
        player1Points = 0;
        player2Points = 0;
        updatePointsText();
        resetBoard();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putInt("roundCount", roundCount);
        outState.putInt("player1Points", player1Points);
        outState.putInt("player2Points", player2Points);
        outState.putBoolean("player1Turn", player1Turn);

        for(int i = 0; i < 3; ++i)
        {
            for(int j = 0; j < 3; ++j)
            {
                if(buttons[i][j].getDrawable().equals(troyIcon))
                {
                    outState.putInt("tile" + i + j, R.drawable.troy_icon);
                }
                else if(buttons[i][j].getDrawable().equals(timmoIcon))
                {
                    outState.putInt("tile" + i + j, R.drawable.timmo_icon);
                }
                else
                {
                    outState.putInt("tile" + i + j, R.drawable.null_icon);
                }
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        roundCount = savedInstanceState.getInt("roundCount");
        player1Points = savedInstanceState.getInt("player1Points");
        player2Points = savedInstanceState.getInt("player2Points");
        player1Turn = savedInstanceState.getBoolean("player1Turn");

        for(int i = 0; i < 3; ++i)
        {
            for(int j = 0; j < 3; ++j)
            {
                buttons[i][j].setImageDrawable(getResources().getDrawable(savedInstanceState.getInt("tile" + i + j)));
            }
        }
    }
}
