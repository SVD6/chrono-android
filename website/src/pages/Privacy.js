import back from '../assets/back-button.svg';

const Privacy = (props) => {
    const { setToggle } = props;
    
    return(
    <div className="privacy">
        <a href="/"><img src={back} className="privacy-back-button" alt="Back"/></a>
        <h1 className="privacy-title">Chrono Privacy Policy</h1>
        <p><strong>Last Updated:</strong> January 7th 2021.</p>
        <hr></hr>
        <p>Thank you for using Chrono.</p>
        <p>Sai Vikranth Desu built the Chrono app as an Ad Supported app. This SERVICE is provided by Sai Vikranth Desu at no cost and is intended for use as is. This privacy policy is used to inform visitors regarding the ways in which we gather, use, process, and disclose personal information across the Chrono application. </p>
        <h4>Information Collection and Use</h4>
        <p>Any information that may be requested while using the Chrono application will be retained on your device and is not collected by me in any way. At this moment in time, the Chrono application does not collect any personal information of the user.</p>
        <p>The application does however use third party services that may collect information used to identify you. The app does use third party services that may collect information used to identify you. Link to privacy policy of third party service providers used by the app:</p>
        <ul>
            <li><a href="https://policies.google.com/privacy">Google Play Services</a></li>
            <li><a href="https://firebase.google.com/policies/analytics">Google Analytics for Firebase</a></li>
            <li><a href="https://firebase.google.com/support/privacy/">Firebase Crashlytics</a></li>

        </ul>
        <p>The stated services will collect analytic and app usage data to help me understand how users use this application.</p>
        <h4>Log Data</h4>
        <p>I want to inform you that whenever you use my Service, in a case of an error in the app I collect data and information (through third party products) on your phone called Log Data. This Log Data may include information such as your device Internet Protocol (“IP”) address, device name, operating system version, the configuration of the app when utilizing my Service, the time and date of your use of the Service, and other statistics.</p>
        <h4>Cookies</h4>
        <p>Cookies are files with a small amount of data that are commonly used as anonymous unique identifiers. These are sent to your browser from the websites that you visit and are stored on your device's internal memory.This Service does not use these “cookies” explicitly. However, the app may use third party code and libraries that use “cookies” to collect information and improve their services. You have the option to either accept or refuse these cookies and know when a cookie is being sent to your device. If you choose to refuse our cookies, you may not be able to use some portions of this Service.</p>
        <h4>Links to Other Sites</h4>
        <p>This Service may contain links to other sites. If you click on a third-party link, you will be directed to that site. Note that these external sites are not operated by me. Therefore, I strongly advise you to review the Privacy Policy of these websites. I have no control over and assume no responsibility for the content, privacy policies, or practices of any third-party sites or services.</p>
        <h4>Children’s Privacy</h4>
        <p>These Services do not address anyone under the age of 13. I do not knowingly collect personally identifiable information from children under 13. In the case I discover that a child under 13 has provided me with personal information, I immediately delete this from our servers. If you are a parent or guardian and you are aware that your child has provided us with personal information, please contact me so that I will be able to do necessary actions.</p>
        <h4>Changes to this Privacy Policy</h4>
        <p>I update this Privacy Policy from time to time. When changes are made to this Privacy Policy, a revised copy will be published on this page with updated publish date.</p>
        <h4>Contact</h4>
        <p>If you have any questions or suggestions about my Privacy Policy, do not hesitate to contact me at privacy@chronofit.ca.</p>
    </div>
    )
  };
  
  export default Privacy
