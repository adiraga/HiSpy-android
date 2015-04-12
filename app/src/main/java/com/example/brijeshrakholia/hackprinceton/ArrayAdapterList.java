//package com.example.brijeshrakholia.hackprinceton;
//
//import android.app.Activity;
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//
///**
// * Created by brijeshrakholia on 4/12/15.
// */
//public class ArrayAdapterList extends ArrayAdapter<Prompt> {
//
//    Context mContext;
//    int layoutResourceId;
//    Prompt data[] =null;
//
//
//    public ArrayAdapterList(AnswerQuestion.AsyncTest context, int resource, Prompt[] data) {
//        super(context, resource);
//        this.mContext = mContext;
//        this.data = data;
//
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        if (convertView==null) {
//            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
//            convertView = inflater.inflate(layoutResourceId, parent, false);
//        }
//
//        Prompt promptItem = data[position];
//
//        Button _button = (Button) convertView.findViewById(R.id.buttonItem);
//        _button.setText(promptItem._question);
//        _button.setTag(promptItem._id);
//
//        return convertView;
//    }
//}
