package net.comcraft.src;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

public final class RMS {

    private RecordStore recordStore;

    private RMS(String recordStoreName, boolean create) {
        try {
            recordStore = RecordStore.openRecordStore(recordStoreName, create);
        } catch (RecordStoreException ex) {
            //#debug debug
            ex.printStackTrace();
            throw new ComcraftException("Can't open record store! Record store name: " + recordStoreName + " create if nesescary: " + create, ex);
        }
    }

    public static RMS openRecordStore(String recordStoreName, boolean create) {
        return new RMS(recordStoreName, create);
    }

    public DataInputStream getRecord(int id) {
        try {
            return new DataInputStream(new ByteArrayInputStream(recordStore.getRecord(id)));
        } catch (RecordStoreException ex) {
            //#debug debug
            ex.printStackTrace();
            throw new ComcraftException("Exception while getting record in record store!", ex);
        }
    }

    public void addRecord() {
        try {
            recordStore.addRecord(new byte[1], 0, 1);
        } catch (RecordStoreException ex) {
            //#debug debug
            ex.printStackTrace();
            throw new ComcraftException("Exception while adding record in record store!", ex);
        }
    }

    public void setRecord(int id, ByteArrayOutputStream byteArrayOutputStream) {
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        
        try {
            recordStore.setRecord(id, byteArray, 0, byteArray.length);
        } catch (RecordStoreException ex) {
            //#debug debug
            ex.printStackTrace();
            throw new ComcraftException("Exception while setting record in record store! Record id: " + id, ex);
        }
    }

    public boolean hasAnyRecord() {
        try {
            return recordStore.getNumRecords() != 0;
        } catch (RecordStoreNotOpenException ex) {
            //#debug debug
            ex.printStackTrace();
            throw new ComcraftException("Exception while checking if record store has any records! Record strore isn't opened.", ex);
        }
    }
    
    public void closeRecordStore() {
        try {
            recordStore.closeRecordStore();
        } catch (RecordStoreException ex) {
            //#debug debug
            ex.printStackTrace();
            throw new ComcraftException("Exception while closing record store !", ex);
        }
    }
}
