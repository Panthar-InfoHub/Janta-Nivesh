import SwiftUI
import UIKit
import FirebaseCore
import FirebaseInstallations
import FirebaseMessaging
import UserNotifications
import Shared

class AppDelegate: NSObject, UIApplicationDelegate, MessagingDelegate, UNUserNotificationCenterDelegate {

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        FirebaseApp.configure()

        Messaging.messaging().delegate = self
        UNUserNotificationCenter.current().delegate = self

        UNUserNotificationCenter.current().requestAuthorization(
            options: [.alert, .badge, .sound]
        ) { _, error in
            if let error {
                print("Notification permission request failed: \(error)")
            }
        }
        // Firebase's app delegate swizzling hands the APNs token to FCM once this succeeds.
        application.registerForRemoteNotifications()

        return true
    }

    func application(
        _ application: UIApplication,
        didFailToRegisterForRemoteNotificationsWithError error: Error
    ) {
        print("APNs registration failed: \(error)")
    }

    /**
     * FCM registers this app instance by its Firebase Installation ID (FirebaseMessagingInstallationIdEnabled
     * in Info.plist). The backend receives the same ID at login, through FirebaseInstallationIdBridge.
     */
    func messaging(_ messaging: Messaging, didReceiveRegistration installationId: String?) {
        print("FCM registered with installation ID: \(installationId ?? "nil")")
    }

    /** Shows notifications that arrive while the app is open, instead of dropping them. */
    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        willPresent notification: UNNotification,
        withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void
    ) {
        completionHandler([.banner, .sound, .badge])
    }
}

/** Hands the Firebase Installation ID to the shared code, which sends it to the backend at login. */
final class FirebaseInstallationIdBridge: NSObject, FirebaseInstallationIdProvider {
    func fetchInstallationId(onResult: @escaping (String?) -> Void) {
        Installations.installations().installationID { id, error in
            if let error {
                print("Firebase installation ID fetch failed: \(error)")
            }
            onResult(id)
        }
    }
}

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
