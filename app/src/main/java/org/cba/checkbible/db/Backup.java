package org.cba.checkbible.db;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.cba.checkbible.CheckBibleApp;
import org.cba.checkbible.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jinhwan.na on 2017-05-23.
 */

public class Backup {
    private static final String TAG = Backup.class.getSimpleName();
    static final String DB_NAME = "checkbible";
    private final String mBackUpDirectory = Environment.getExternalStorageDirectory() + "/CheckBible";
    private Context mContext;

    public Backup(Context context) {
        mContext = context;
    }

    private void createFolder() {
        File sd = new File(mBackUpDirectory);
        if (!sd.exists()) {
            sd.mkdir();
        }
    }

    public void exportDB() {
        try {
            createFolder();

            File sd = new File(mBackUpDirectory);

            if (sd.canWrite()) {
                SimpleDateFormat formatTime = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                String backupDBPath = DB_NAME + "_" + formatTime.format(new Date());

                String currentDBPath = "//data//" + CheckBibleApp.getContext().getPackageName()
                        + "//databases//" + DB_NAME;

                File currentDB = new File(Environment.getDataDirectory(), currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(CheckBibleApp.getContext(), "Backup Successful to Internal storage/CheckBible/"+ backupDBPath,
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(CheckBibleApp.getContext(), "Backup Failed!", Toast.LENGTH_SHORT)
                    .show();

        }
    }

    public void showDialLog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.backup_data).setIcon(R.mipmap.ic_launcher)
                .setMessage(R.string.backup_before_import);
        builder.setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showDialogListFile();
//                exportToSD();
            }
        });
        builder.show();
    }

    public void showDialogListFile() {
        createFolder();

        File forder = new File(mBackUpDirectory);
        File[] listFile = forder.listFiles();

        final String[] listFileName = new String[listFile.length];
        for (int i = 0, j = listFile.length - 1; i < listFile.length; i++, j--) {
            listFileName[j] = listFile[i].getName();
        }

        if (listFileName.length > 0) {
            // get layout for list
            LayoutInflater inflater = ((FragmentActivity) mContext).getLayoutInflater();
            View convertView = inflater.inflate(R.layout.list_backup_file, null);
            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

            // set view for dialog
            builder.setView(convertView);
            builder.setTitle(R.string.select_file).setIcon(R.mipmap.ic_launcher);

            final AlertDialog alert = builder.create();

            ListView lv = (ListView) convertView.findViewById(R.id.lv_backup);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                    android.R.layout.simple_list_item_1, listFileName);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    alert.dismiss();
                    importDB(listFileName[position]);
                }
            });
            alert.show();
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(R.string.delete).setIcon(R.mipmap.ic_launcher)
                    .setMessage(R.string.backup_empty);
            builder.show();
        }
    }

    public void importDB(String fileNameOnSD) {

        File sd = new File(mBackUpDirectory);
        if (sd.canWrite()) {
            String currentDBPath = "//data//" + CheckBibleApp.getContext().getPackageName()
                    + "//databases//" + DB_NAME;

            File currentDB = new File(Environment.getDataDirectory(), currentDBPath);
            File backupDB = new File(sd, fileNameOnSD);
            try {
                FileChannel src = new FileInputStream(backupDB).getChannel();
                FileChannel dst = new FileOutputStream(currentDB)
                        .getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                onBackupListener.onFinishImport();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
            }
        }
    }

    private OnBackupListener onBackupListener;

    public void setOnBackupListener(OnBackupListener onBackupListener) {
        this.onBackupListener = onBackupListener;
    }

    public interface OnBackupListener {
        void onFinishImport();
    }
}
