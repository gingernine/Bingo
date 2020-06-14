package board1;

import static board1.Board.*;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.websocket.Session;

public class Roulette extends JFrame {

	JButton button;

	public static void main(String[] args) {
		Roulette frame = new Roulette("Roulette");
		frame.setVisible(true);
	}

	Roulette(String title) {
		setTitle(title);
		setBounds(100, 100, 100, 100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		button = new JButton("Roulette");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Random rand = new Random();
				Integer x = rand.nextInt(75) + 1;
				ArrayList<Session> ses = getSessionSet();
				System.out.println(ses);

				for (Session s : ses) {
					System.out.println(x);
					sendMessage(x.toString(), s);
				}
			}
		});
		button.setPreferredSize(new Dimension(100, 50));

		JPanel p = new JPanel();
		p.add(button);

		Container contentPane = getContentPane();
		contentPane.add(p, BorderLayout.CENTER);
	}
}