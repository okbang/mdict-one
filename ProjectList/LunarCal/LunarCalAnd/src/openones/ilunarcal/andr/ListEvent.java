/**
 *
 */
package openones.ilunarcal.andr;

import java.util.Calendar;
import java.util.Locale;

import openones.ilunarcal.andr.R;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The Class ListEvent.
 *
 * @author mastertan
 */

public class ListEvent extends ListActivity {

    /** The model. */
    private Cursor model = null;

    /** The adapter. */
    private EventAdapter adapter = null;

    /** The helper. */
    private EventHelper helper = null;

    /** The event name. */
    private EditText eventName = null;

    /** The description. */
    private EditText description = null;

    /** The date plan. */
    private EditText datePlan = null;

    /** The cur click date. */
    private String curClickDate = null;

    /** The day2. */
    private ILunarCalActivity day2 = new ILunarCalActivity();

    /** The year. */
    private int day, month, year;

    /** The calendar. */
    private Calendar calendar;

    /** The event id. */
    private String eventId = null;

    /** The Constant ID_EXTRA. */
    static final String ID_EXTRA = "android.ilunar._ID";

    /** The select all. */
    private static boolean selectAll;

    /** The Constant DELETE_DIALOG. */
    private static final int DELETE_DIALOG = 1;

    /**
     * [Explain the description for this method here].
     *
     * @param savedInstanceState the saved instance state
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listevent);

        helper = new EventHelper(this);

        calendar = Calendar.getInstance(Locale.getDefault());
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);

        // if (selectAll == true) {
        model = helper.selectAll();
        startManagingCursor(model);
        adapter = new EventAdapter(model);
        setListAdapter(adapter);
        registerForContextMenu(getListView());
        Toast.makeText(
                ListEvent.this,
                "Có tất cả " + getListView().getCount()
                        + " sự kiện đã được tạo.", Toast.LENGTH_LONG).show();
        // selectAll = false;
        // } else {
        // curClickDate = getIntent().getStringExtra("Date");
        // model = helper.getAll(curClickDate);
        // startManagingCursor(model);
        // adapter = new EventAdapter(model);
        // setListAdapter(adapter);
        // registerForContextMenu(getListView());
        // if (getListView().getCount() == 0) {
        // Intent intent = new Intent(ListEvent.this, DetailForm.class);
        // startActivity(intent);
        // finish();
        // //Toast.makeText(ListEvent.this, "Không có sự kiện nào trong ngày " +
        // abc + " !!!" + '\n' + "Bấm MENU để tạo sự kiện",
        // Toast.LENGTH_LONG).show();
        // } else {
        // Toast.makeText(ListEvent.this, getListView().getCount() +
        // " sự kiện trong ngày " + curClickDate, Toast.LENGTH_LONG).show();
        // }
        // }

    }

    /**
     * Checks if is select all.
     *
     * @return the selectAll
     */
    public static boolean isSelectAll() {
        return selectAll;
    }

    /**
     * Sets the select all.
     *
     * @param selectAll the selectAll to set
     */
    public static void setSelectAll(boolean selectAll) {
        ListEvent.selectAll = selectAll;
    }

    /**
     * [Explain the description for this method here].
     * @see android.app.ListActivity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        helper.close();
    }

    /**
     * [Explain the description for this method here].
     *
     * @param list the list
     * @param view the view
     * @param position the position
     * @param id the id
     * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
     */
    @Override
    protected void onListItemClick(ListView list, View view, int position,
            long id) {
        // TODO Auto-generated method stub
        finish();
        Intent i = new Intent(ListEvent.this, DetailForm.class);
        i.putExtra(ID_EXTRA, String.valueOf(id));
        startActivity(i);
    }

    /**
     * [Explain the description for this method here].
     *
     * @param item the item
     * @return true, if successful
     * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getTitle() == "Xóa sự kiện") {
            showDialog(DELETE_DIALOG);
        }
        return false;

    }

    /**
     * [Explain the description for this method here].
     *
     * @param menu the menu
     * @param v the v
     * @param menuInfo the menu info
     * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu, android.view.View,
     * android.view.ContextMenu.ContextMenuInfo)
     */

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(helper.getEventName(model));
        menu.add(0, v.getId(), 0, "Xóa sự kiện");
    }

    /**
     * [Explain the description for this method here].
     *
     * @param menu the menu
     * @return true, if successful
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        new MenuInflater(this).inflate(R.menu.option, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * [Explain the description for this method here].
     *
     * @param item the item
     * @return true, if successful
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == R.id.add) {
            String date = day + "-" + month + "-" + year;
            callIntentAdd(date);
            return true;
        } else if (item.getItemId() == R.id.back) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Call intent_add.
     *
     * @param value the value
     */
    public void callIntentAdd(String value) {
        Intent intent = new Intent(ListEvent.this, DetailForm.class);
        intent.putExtra("Date", value);
        ListEvent.this.startActivity(intent);
    }

    /**
     * The Class EventAdapter.
     */
    class EventAdapter extends CursorAdapter {

        /**
         * Instantiates a new event adapter.
         *
         * @param c the c
         */
        EventAdapter(Cursor c) {
            super(ListEvent.this, c);
        }

        /**
         * [Explain the description for this method here].
         *
         * @param row the row
         * @param ctxt the ctxt
         * @param c the c
         */
        @Override
        public void bindView(View row, Context ctxt, Cursor c) {
            // TODO Auto-generated method stub
            EventHolder holder = (EventHolder) row.getTag();
            holder.populateFrom(c, helper);
        }

        /**
         * [Explain the description for this method here].
         *
         * @param ctxt the ctxt
         * @param c the c
         * @param parent the parent
         * @return the view
         */
        @Override
        public View newView(Context ctxt, Cursor c, ViewGroup parent) {
            // TODO Auto-generated method stub
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.row, parent, false);
            EventHolder holder = new EventHolder(row);
            row.setTag(holder);
            return (row);
        }
    }

    /**
     * The Class EventHolder.
     */
    static class EventHolder {

        /** The event name. */
        private TextView eventName = null;

        /** The description. */
        private TextView description = null;

        /** The date plan. */
        private TextView datePlan;

        /**
         * Instantiates a new event holder.
         *
         * @param row the row
         */
        public EventHolder(View row) {
            // TODO Auto-generated constructor stub
            eventName = (TextView) row.findViewById(R.id.event_name);
            description = (TextView) row.findViewById(R.id.description);
            datePlan = (TextView) row.findViewById(R.id.datePlan);
        }

        /**
         * Populate from.
         *
         * @param c the c
         * @param helper the helper
         */
        void populateFrom(Cursor c, EventHelper helper) {
            eventName.setText("Tên sự kiện: " + helper.getEventName(c));
            description.setText("Mô tả: " + helper.getDescription(c));
            datePlan.setText("Ngày bắt đầu: " + helper.getStartDay(c)
                    + " Giờ bắt đầu: " + helper.getStartTime(c));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onCreateDialog(int)
     */
    /**
     * [Explain the description for this method here].
     *
     * @param id the id
     * @return the dialog
     * @see android.app.Activity#onCreateDialog(int)
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        switch (id) {
        case DELETE_DIALOG:
            return new AlertDialog.Builder(ListEvent.this)
                    .setIcon(R.drawable.alert_dialog_icon)
                    .setTitle(
                            "Xóa sự kiện " + helper.getEventName(model) + " ?")
                    .setPositiveButton("Đồng ý",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int whichButton) {

                                    /* User clicked OK so do some stuff */
                                    String tmp = Integer.toString(helper
                                            .getEventId(model));
                                    helper.delete(tmp);
                                    model.requery();
                                }
                            })
                    .setNegativeButton("Hủy",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int whichButton) {

                                    /* User clicked Cancel so do some stuff */
                                    model.requery();
                                }
                            }).create();
            default :
                break;
        }
        return null;
    }
}
