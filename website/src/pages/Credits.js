import back from '../assets/back-button.svg';

const Credits = (props) => {
    const { setToggle } = props;
    
    return(
    <div className="credits">
        <a href="/"><img src={back} className="back-button" alt="Back" /></a>
        <h1 className="credits-title">A special thanks to</h1> 
        <hr></hr>
        <div className="credits-text">
            <h2 className="credits-text"> 
                <a href="https://www.flaticon.com/authors/pixel-perfect" title="Pixel perfect"> Pixel perfect </a> 
                 from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com </a>
                 for the fantastic icons
            </h2>
        </div>
        <div className="credits-text">
            <h2 className="credits-text"> 
                <a href="https://www.freesoundslibrary.com/referee-blowing-whistle-sound-effect/" title="Free Sounds Library"> Free Sounds Library </a> 
                from <a href="http://www.freesoundslibrary.com" title="freesoundslibrary.com">www.freesoundslibrary.com </a> 
                for the awesome sound effects
            </h2>

            {/* <ul>
                <h2 className="credits-text">Effects Used:</h2>
                <li><a href="https://www.freesoundslibrary.com/referee-blowing-whistle-sound-effect/">Whistle Sound Effect</a></li>
            </ul> */}
        </div>
        <div className="credits-text">
            <h2 className="credits-text"> 
                <a href="https://www.zapsplat.com/music" title="ZapSplat"> ZapSplat </a> 
                from <a href="https://www.zapsplat.com/music" title="ZapSplat">www.zapsplat.com </a> 
                for the incredible sound effects
            </h2>
        </div>
        <div className="credits-text">
            <h2 className="credits-text">
                <a href="https://assets10.lottiefiles.com/packages/lf20_mskGaJ.json" title="Lottie Animations"> Lottie Files </a> 
                 for the smooth animations
            </h2>
        </div>
    </div>
    )
  };
  
  export default Credits;
