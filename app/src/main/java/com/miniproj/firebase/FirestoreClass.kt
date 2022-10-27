package com.miniproj.firebase

import android.app.Activity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.miniproj.activities.BaseActivity
import com.miniproj.activities.DetailsActivity
import com.miniproj.activities.SignInActivity
import com.miniproj.activities.SignUpActivity
import com.miniproj.model.User
import com.miniproj.utils.Constants

class FirestoreClass: BaseActivity() {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun signUpUser(activity: SignUpActivity, userInfo: User) {

        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.signUpSuccess()
            }
            .addOnFailureListener {
                showErrorSnackBar("firestore sign up failed")
            }
    }

    fun signInUser(activity: SignInActivity) {

        FirebaseFirestore.getInstance().collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                val loggedInUser = document.toObject(User::class.java)!!

                activity.signInSuccess(loggedInUser)
            }
            .addOnFailureListener {
                showErrorSnackBar("firestore sign in failed")
            }
    }
}