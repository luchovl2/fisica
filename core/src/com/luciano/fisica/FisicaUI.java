package com.luciano.fisica;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.luciano.fisica.utils.Constants;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class FisicaUI implements ActionListener
{
    private JFrame frame;
    private JFrame frame2;
    private JTextField textMasa;
    private JTextField textRozamiento;
    private JTextField textPendiente;
    private JTextField textPosX;
    private JTextField textPosY;
    private JTextField textAlto;
    private JTextField textAncho;
    private JButton button5;

    private FisicaScreen.Formas agregarForma;

    public FisicaUI()
    {
    }

    public void init()
    {
        frame = new JFrame();

        JButton button = new JButton("Bloque");
        JButton button2 = new JButton("Bola");
        JButton button3 = new JButton("Rampa");
        JButton button4 = new JButton("Polea");
        button5 = new JButton("Agregar");
        JButton button6 = new JButton("Viga");

        button.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);
        button4.addActionListener(this);
        button5.addActionListener(this);
        button6.addActionListener(this);

        frame.setSize(Constants.UI_ANCHO, Constants.UI_ALTO);
        frame.setLayout(new GridLayout(2, 2, 15, 15));
        frame.setLocation(Constants.UI_POSX, 10);
        frame.setTitle("Frame 1");
        
        frame.add(button);
        frame.add(button2);
        frame.add(button3);
        frame.add(button4);
        frame.add(button6);

        frame.setVisible(true);

        frame2 = new JFrame();
        frame2.setLayout(new BoxLayout(frame2.getContentPane(), BoxLayout.Y_AXIS));
        frame2.setSize(Constants.UI_ANCHO, Constants.UI_ALTO);
        frame2.setLocation(Constants.UI_POSX, 10+Constants.UI_ALTO);
        frame2.setTitle("Frame 2");

        textMasa = new JTextField(12);
        textPendiente = new JTextField(12);
        textRozamiento = new JTextField(12);
        textPosX = new JTextField(5);
        textPosY = new JTextField(5);
        textAlto = new JTextField(5);
        textAncho = new JTextField(5);

        button5.setEnabled(false);

        frame2.add(new JLabel("Masa"));
        frame2.add(textMasa);
        frame2.add(new JLabel("Posicion x"));
        frame2.add(textPosX);
        frame2.add(new JLabel("Posicion y"));
        frame2.add(textPosY);
        frame2.add(new JLabel("Ancho"));
        frame2.add(textAncho);
        frame2.add(new JLabel("Alto"));
        frame2.add(textAlto);
        frame2.add(new JLabel("Pendiente"));
        frame2.add(textPendiente);
        frame2.add(new JLabel("Rozamiento"));
        frame2.add(textRozamiento);

        frame2.add(button5);

        frame2.pack();
        frame2.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        if(actionEvent.getSource() instanceof JButton)
        {
            JButton boton = (JButton)actionEvent.getSource();

            Gdx.app.log("Boton", boton.getText());

            if(boton.getText().equals("Bloque"))
            {
                initText();
                button5.setEnabled(true);

                agregarForma = FisicaScreen.Formas.CAJA;
                frame2.setTitle("Bloque");
            }
            else if(boton.getText().equals("Bola"))
            {
                initText();
                button5.setEnabled(true);

                agregarForma = FisicaScreen.Formas.BOLA;
                frame2.setTitle("Bola");
            }
            else if(boton.getText().equals("Rampa"))
            {
                initText();
                button5.setEnabled(true);

                agregarForma = FisicaScreen.Formas.RAMPA;
                frame2.setTitle("Rampa");
            }
            else if(boton.getText().equals("Polea"))
            {
                initText();
                button5.setEnabled(true);
                agregarForma = FisicaScreen.Formas.POLEA;
                frame2.setTitle("Polea");
            }
            else if(boton.getText().equals("Viga"))
            {
                initText();
                button5.setEnabled(true);

                agregarForma = FisicaScreen.Formas.VIGA;
                frame2.setTitle("Viga");
            }
            else if(boton.getText().equals("Agregar"))
            {
                boton.setEnabled(false);

                float posX = Float.valueOf(textPosX.getText().trim());
                float posY = Float.valueOf(textPosY.getText().trim());
                float masa = Float.valueOf(textMasa.getText().trim());
                float roza = Float.valueOf(textRozamiento.getText().trim());
                float pendiente = Float.valueOf(textPendiente.getText().trim());
                float alto = Float.valueOf(textAlto.getText().trim());
                float ancho = Float.valueOf(textAncho.getText().trim());

                if(agregarForma == FisicaScreen.Formas.CAJA)
                {
                    FisicaGame.screen.agregarCuerpo(posX, posY, ancho, alto, FisicaScreen.Formas.CAJA);
                }
                else if(agregarForma == FisicaScreen.Formas.BOLA)
                {
                    FisicaGame.screen.agregarCuerpo(posX, posY, ancho, alto, FisicaScreen.Formas.BOLA);
                }
                else if(agregarForma == FisicaScreen.Formas.RAMPA)
                {
                    pendiente = pendiente * MathUtils.PI / 180f;
                    ancho = alto * MathUtils.cos(pendiente) / MathUtils.sin(pendiente);

                    FisicaGame.screen.agregarObstaculo(posX, posY, ancho, alto, roza, FisicaScreen.Formas.RAMPA);
                }
                else if(agregarForma == FisicaScreen.Formas.VIGA)
                {
                    FisicaGame.screen.agregarObstaculo(posX, posY, ancho, alto, roza, FisicaScreen.Formas.VIGA);
                }
                else if(agregarForma == FisicaScreen.Formas.POLEA)
                {
                    Body bodyA = FisicaGame.screen.cuerpos.get(0).body;
                    Body bodyB = FisicaGame.screen.cuerpos.get(1).body;
                    Body groundA = FisicaGame.screen.obstaculos.get(1).body;
                    Body groundB = FisicaGame.screen.obstaculos.get(2).body;

                    FisicaGame.screen.agregarPolea(bodyA, bodyB, groundA, groundB);
                }
            }
        }
    }

    private void initText()
    {
        textMasa.setText("0.2");
        textRozamiento.setText("1");
        textPosX.setText("1");
        textPosY.setText("1");
        textAncho.setText("0.2");
        textAlto.setText("0.2");
        textPendiente.setText("30");
    }
}