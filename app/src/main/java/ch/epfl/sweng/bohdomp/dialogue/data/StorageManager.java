package ch.epfl.sweng.bohdomp.dialogue.data;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * Class representing a storage manager. Used to make app statefull
 */
public class StorageManager {


    private final String mSaveFileName ="saveFile";
    private final Context mContext;

    public StorageManager(Context context) {
        Contract.throwIfArgNull(context, "Context is null in StorageManager");

        this.mContext = context;
    }



    public void saveData() {

        FileOutputStream outputStream = null;

        Bundle bundle = DefaultDialogData.getInstance().createBundle();

        Parcel parcel = Parcel.obtain();
        bundle.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);


        try {
            outputStream = mContext.openFileOutput(mSaveFileName, mContext.MODE_PRIVATE);
            outputStream.write(parcel.marshall());


        } catch (IOException e) {
            Toast.makeText(mContext, "Unable to save messages", Toast.LENGTH_LONG).show();

        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    Toast.makeText(mContext, "Unable to save messages while closing the file",
                            Toast.LENGTH_LONG).show();

                }
            }
        }


    }

    public void retreiveData() {
        Parcel parcel = Parcel.obtain();

        byte [] data = readFile();
        if (data != null) {
            parcel.unmarshall(data, 0, data.length);

            parcel.setDataPosition(0);

            DefaultDialogData.getInstance().restoreFromBundle(parcel.readBundle());


        }
        parcel.recycle();

    }

    private byte[] readFile() {
        byte[] data = null;
        FileInputStream inputStream = null;
        try {
            inputStream = mContext.openFileInput(mSaveFileName);
            data = new byte[inputStream.available()];
            inputStream.read(data);

        } catch (IOException e) {
            Toast.makeText(mContext, "Unable to retreive old messages", Toast.LENGTH_LONG).show();

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Toast.makeText(mContext, "Unable to retreive old messages while closing the file",
                            Toast.LENGTH_LONG).show();
                }
            }
        }

        return data;
    }




}
