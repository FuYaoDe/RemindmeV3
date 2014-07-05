/**
 * 
 */
package me.iamcxa.remindme.cardfragment;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardCursorAdapter;
import it.gmariotti.cardslib.library.internal.CardExpand;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.internal.ViewToClickToExpand;
import it.gmariotti.cardslib.library.view.component.CardHeaderView;
import me.iamcxa.remindme.R;
import me.iamcxa.remindme.RemindmeVar;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import me.iamcxa.remindme.cardfragment.SetCardFromCursor;

/**
 * @author cxa
 * 
 */

/*******************************/
/** Class MyCursorCardAdapter **/
/*******************************/
public class MyCursorCardAdapter extends CardCursorAdapter {
	private static SetCardFromCursor mSetCardFromCursor;
	private static OnClickCard mReadCardOnClick;

	public MyCursorCardAdapter(Context context) {
		super(context);
	}

	@Override
	protected Card getCardFromCursor(final Cursor cursor) {
		MyCursorCard card = new MyCursorCard(super.getContext());
		
		mReadCardOnClick = new OnClickCard(getContext(), cursor, card);
		mReadCardOnClick.setMyCursorCardAdapter(this);

		mSetCardFromCursor = new SetCardFromCursor(getContext(), cursor, card);
		mSetCardFromCursor.setIt();

		card.setClickable(true);
		card.setExpanded(true);

		// Create a CardHeader
		CardHeader header = new CardHeader(getContext());

		// Set visible the expand/collapse button
		// header.setButtonExpandVisible(true);
		header.setOtherButtonVisible(true);
		header.setOtherButtonDrawable(R.drawable.ic_action_labels);

		// Add a callback
		header.setOtherButtonClickListener(new CardHeader.OnClickCardHeaderOtherButtonListener() {
			@Override
			public void onButtonItemClick(Card card, View view) {
				Toast.makeText(getContext(), "Click on Other Button",
						Toast.LENGTH_LONG).show();

			}
		});

		// Set the header title
		header.setTitle(card.mainHeader);

		// Add Header to card
		card.addCardHeader(header);

		// This provides a simple (and useless) expand area
		CardExpand expand = new CardExpand(getContext());
		// Set inner title in Expand Area
		String aa = "dbId=" + cursor.getString(0) + ",w="
				+ cursor.getString(RemindmeVar.TaskCursor.KEY_INDEX.PRIORITY)
				+ "cardID="+card.getId();

		expand.setTitle(aa);
		card.addCardExpand(expand);

		// Add Thumbnail to card
		final MyCardThumbnail thumb = new MyCardThumbnail(getContext());
		thumb.setDrawableResource(card.resourceIdThumb);
		card.addCardThumbnail(thumb);

		// Set on Click listener
		card.setOnClickListener(new Card.OnCardClickListener() {
			@Override
			public void onClick(Card card, View view) {

				
				mReadCardOnClick.readIt(card.getId());

			}
		});

		card.setOnLongClickListener(new Card.OnLongCardClickListener() {
			@Override
			public boolean onLongClick(Card card, View view) {
				// TODO Auto-generated method stubs

				ShowLongClickMenu();

				return false;

			}
		});

		return card;
	}

	// private void setCardFromCursor(MyCursorCard card, Cursor cursor) {
	//
	// setCardFromCursor(card, cursor);
	//
	// }

	/***********************/
	/** Class MyThumbnail **/
	/***********************/
	// implment the clickable card thumbnail.
	class MyCardThumbnail extends CardThumbnail {

		public MyCardThumbnail(Context context) {
			super(context);
		}

		@Override
		public void setupInnerViewElements(ViewGroup parent, View imageView) {
			ViewToClickToExpand viewToClickToExpand = ViewToClickToExpand
					.builder().highlightView(true).setupView(imageView);
			getParentCard().setViewToClickToExpand(viewToClickToExpand);
		}
	}

	/***********************/
	/** Class MyCardHeader **/
	/***********************/
	// implment the clickable card thumbnail.
	class MyCardHeader extends CardHeaderView {

		@Override
		@SuppressLint("NewApi")
		protected void setupButtons() {
			// TODO Auto-generated method stub
			super.setupButtons();
		}

		@Override
		protected void setupInnerView() {
			// TODO Auto-generated method stub
			super.setupInnerView();

		}

		@Override
		public OnClickListener getOnClickExpandCollapseActionListener() {
			// TODO Auto-generated method stub
			ViewToClickToExpand viewToClickToExpand = ViewToClickToExpand
					.builder().highlightView(true)
					.setupView(getImageButtonOther());
			getCardFromCursor(getCursor()).setViewToClickToExpand(
					viewToClickToExpand);

			return super.getOnClickExpandCollapseActionListener();
		}

		@Override
		public ImageButton getImageButtonOverflow() {
			// TODO Auto-generated method stub
			return super.getImageButtonOverflow();
		}

		@Override
		public ImageButton getImageButtonExpand() {
			// TODO Auto-generated method stub
			return super.getImageButtonExpand();
		}

		@Override
		public ImageButton getImageButtonOther() {
			// TODO Auto-generated method stub
			return super.getImageButtonOther();
		}

		public MyCardHeader(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

	}

	/************************/
	/** Class MyCursorCard **/
	/************************/
	class MyCursorCard extends Card {

		String DateTime;
		String LocationName;
		String Notifications;
		String mainHeader;

		int resourceIdThumb;

		public MyCursorCard(Context context) {
			super(context, R.layout.card_cursor_inner_content);

		}

		@Override
		public void setupInnerViewElements(ViewGroup parent, View view) {

			// Retrieve elements
			TextView mTitleTextView = (TextView) parent
					.findViewById(R.id.card_cursor_main_inner_title);
			TextView mSecondaryTitleTextView = (TextView) parent
					.findViewById(R.id.card_cursor_main_inner_subtitle);
			TextView mThirdTitleTextView = (TextView) parent
					.findViewById(R.id.card_cursor_main_inner_thirdtitle);

			if (mTitleTextView != null)
				mTitleTextView.setText(DateTime);

			if (mSecondaryTitleTextView != null)
				mSecondaryTitleTextView.setText(LocationName);

			if (mThirdTitleTextView != null)
				mThirdTitleTextView.setText(Notifications);

		}
	}

	private void removeCard() {

		// Use this code to delete items on DB
		ContentResolver resolver = getContext().getContentResolver();
		
		long noDeleted = resolver.delete(RemindmeVar.CONTENT_URI,
				RemindmeVar.TaskCursor.KEY.KEY_ID + " = ? ",
				new String[] { this.getCardFromCursor(getCursor()).getId() });

		this.notifyDataSetChanged();
	

	}



	private AlertDialog ShowLongClickMenu() {

		return new AlertDialog.Builder(getContext())
				.setTitle("請選擇...")
				.setItems(R.array.CardOnLongClickDialogString,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								switch (which) {
								case 0:// 修改

									break;
								case 1:// 刪除
									removeCard();

									break;
								case 2:// 提高優先

									break;
								case 3:// 降低優先

									break;
								case 5:// 分享

									break;
								}

								//
								/* User clicked so do some stuff */
								String[] items = getContext()
										.getResources()
										.getStringArray(
												R.array.CardOnLongClickDialogString);
								new AlertDialog.Builder(getContext())
										.setMessage(
												"You selected: " + which
														+ " , " + items[which])
										.show();
								//

							}
						}).show();

	}

}
