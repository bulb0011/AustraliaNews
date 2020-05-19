package com.ruanyun.australianews.di.module;

import com.ruanyun.australianews.di.ActivityScoped;
import com.ruanyun.australianews.di.FragmentScoped;
import com.ruanyun.australianews.ui.*;
import com.ruanyun.australianews.ui.im.MessageListActivity;
import com.ruanyun.australianews.ui.im.P2pChatActivity;
import com.ruanyun.australianews.ui.life.*;
import com.ruanyun.australianews.ui.life.main.*;
import com.ruanyun.australianews.ui.life.release.*;
import com.ruanyun.australianews.ui.login.*;
import com.ruanyun.australianews.ui.main.*;
import com.ruanyun.australianews.ui.my.*;
import com.ruanyun.australianews.ui.news.*;
import com.ruanyun.australianews.ui.wealth.*;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * We want Dagger.Android to create a Subcomponent which has a parent Component of whichever module
 * ActivityBindingModule is on, in our case that will be AppComponent. The beautiful part about this
 * setup is that you never need to tell AppComponent that it is going to have all these
 * subcomponents nor do you need to tell these subcomponents that AppComponent exists. We are also
 * telling Dagger.Android that this generated SubComponent needs to include the specified modules
 * and be aware of a scope annotation @ActivityScoped When Dagger.Android annotation processor runs
 * it will create 4 subcomponents for us.
 */
@Module
public abstract class ActivityBindingModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract MyFragment myFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract NewsFragment newsFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract NewsListChildFragment newsListChildFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract LifeMyFragment lifeMyFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract YellowPageFragment lifYellowPageFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract LifeHomeFragment lifeHomeFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract SearchNewsListFragment newsSearchListFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract SearchLifeListFragment lifeSearchListFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract MyCollectionNewsListFragment myCollectionNewsListFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract MyCollectionLifeListFragment myCollectionLifeListFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract MyBrowseRecordNewListFragment myBrowseRecordListFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract MyBrowseRecordLifeListFragment myBrowseRecordLifeListFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract MyReleaseLifeListFragment myReleaseLifeListFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract SelectReleaseLifeTypeActivity selectReleaseLifeTypeActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract SplashActivity splashActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract GuideActivity guideActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract WebViewActivity webViewActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract MainActivity mainActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract LifeMainActivity lifeMainActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract LoginActivity loginActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract RegisteredActivity registeredActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract CountryAreaCodeSelectActivity countryAreaCodeSelectActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract ForgetPasswordActivity forgetPasswordActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract ModifyPasswordActivity modifyPasswordActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract CityListActivity cityListActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract MessageListActivity messageListActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract P2pChatActivity p2pChatActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract ChannelManagerActivity channelManagerActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract SearchActivity searchActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract FeedbackActivity feedbackActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract PersonalInformationActivity personalInformationActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract YellowPageListActivity yellowPageListActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract SelectReleaseTypeActivity selectReleaseTypeActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract MyMessageListActivity myMessageListActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract MyMessageNotificationDetailsActivity myMessageNotificationDetailsActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract VideoNewsDetailsActivity videoNewsDetailsActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract MyEvaluationListActivity myEvaluationListActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract MyCollectionActivity myCollectionActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract MyBrowseRecordActivity myBrowseRecordActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract MapPointActivity mapPointActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract ReleaseYellowPageActivity releaseYellowPageActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract HouseRentListActivity houseRentListActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract HouseDemandListActivity houseDemandListActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract BusinessTransferListActivity businessTransferListActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract RecruitmentListActivity recruitmentListActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract CarSaleListActivity carSaleListActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract PetTransactionListActivity petTransactionListActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract TransactionMarketListActivity transactionMarketListActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract TextbookListActivity textbookListActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract MultipleSelectionActivity multipleSelectionActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract ReleaseBusinessTransferActivity releaseBusinessTransferActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract ReleaseCarSaleActivity releaseCarSaleActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract ReleaseHouseDemandActivity releaseHouseDemandActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract ReleaseHouseRentActivity releaseHouseRentActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract ReleasePetTransactionActivity releasePetTransactionActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract ReleaseRecruitmentActivity releaseRecruitmentActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract ReleaseTransactionMarketActivity releaseTransactionMarketActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract ReleaseTextbookActivity releaseTextbookActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract ReleasePetTransactionSelectTypeActivity releasePetTransactionSelectTypeActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract MyReleaseActivity myReleaseActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract MyPushRecordNewListActivity myPushRecordNewListActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract ShopListActivity shopListActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract ReleaseShopActivity releaseShopActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract ReleaseShopGoodsActivity releaseShopGoodsActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract AboutUsListActivity aboutUsListActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract UpdateBindPhoneActivity updateBindPhoneActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract UpdateBindMailboxActivity updateBindMailboxActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract NewsDetailsActivity newsDetailsActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract NewsCommentActivity newsCommentActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract SecondaryCommentActivity secondaryCommentActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract UserHomePageActivity userHomePageActivity();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract HomePageReleaseLifeListFragment homePageReleaseLifeListFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract HomePageEvaluationListFragment homePageEvaluationListFragment();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract WealthActivity wealthActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract WealthMyActivity wealthMyActivity();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract WealthMyFragment wealthMyFragment();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract WealthFundListActivity wealthFundListActivity();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract WealthFundListFragment wealthFundListFragment();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract WealthMyBrowseListActivity wealthMyBrowseListActivity();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract WealthMyBrowseListFragment wealthMyBrowseListFragment();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract ActivitysListActivity activitysListActivity();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract ActivitysListFragment activitysListFragment();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract ReleaseActivitysActivity releaseActivitysActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract HousingMarketListActivity housingMarketListActivity();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract HousingMarketListFragment housingMarketListFragment();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract ReleaseHousingMarketActivity releaseHousingMarketActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract CivilEstateListActivity civilEstateListActivity();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract CivilEstateListFragment civilEstateListFragment();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract ReleaseCivilEstateActivity releaseCivilEstateActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract WealthMyCollectListActivity wealthMyCollectListActivity();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract WealthMyCollectListFragment wealthMyCollecListFragment();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract WealthMyReleaseListActivity wealthMyReleaseListActivity();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract WealthMyReleaseListFragment wealthMyReleaseListFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract HomePageReleaseWealthListFragment homePageReleaseWealthListFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract RegisteredBindActivity RegisteredBindActivity();

}
