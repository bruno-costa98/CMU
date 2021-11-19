package com.example.myapplication2;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.core.app.NotificationCompat;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		createNotificationChannel();
	}

	private void createNotificationChannel() {

		// Cria o canal de notificação para a API 26+
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			int importancia = NotificationManager.IMPORTANCE_DEFAULT;
			NotificationChannel channel = new NotificationChannel("1", "Canal", importancia);
			channel.setDescription("Descricao do canal");

			// Regista o canal no sistema
			NotificationManager notificationManager = getSystemService(NotificationManager.class);
			notificationManager.createNotificationChannel(channel);
		}
	}
	
	public void gerarNotificacao(View view){

		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		PendingIntent p = PendingIntent.getActivity(this, 0, new Intent(this, Atividade2.class), 0);

		NotificationCompat.Builder builder;
		builder = new NotificationCompat.Builder(this, "1");
		builder.setTicker("Texto");
		builder.setContentTitle("Titulo");
		builder.setContentText("Descricao");
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.thiengo));
		builder.setContentIntent(p);

		//NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
		//String [] descs = new String[]{"Descricao 1", "Descricao 2", "Descricao 3", "Descri��o 4"};
		//for(int i = 0; i < descs.length; i++){
		//	style.addLine(descs[i]);
		//}
		//builder.setStyle(style);

		Notification n = builder.build();
		n.flags = Notification.FLAG_AUTO_CANCEL;
		nm.notify(R.drawable.ic_launcher, n);

		try{
			Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			Ringtone toque = RingtoneManager.getRingtone(this, som);
			toque.play();
		}
		catch(Exception e){}
	}
}
