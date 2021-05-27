import react, { Component } from 'react';
import stopwatchGIF from '../assets/gif/stopwatch.gif';
import stopwatchJPG from '../assets/gif/stopwatch.jpg';
import darkModeGIF from '../assets/gif/dark-mode.gif';
import darkModeJPG from '../assets/gif/dark-mode.jpg';
import savedCircuitsGIF from '../assets/gif/circuits-home.gif';
import savedCircuitsJPG from '../assets/gif/circuits-home.jpg';
import FeatureCard from "../components/FeatureCard";

class Features extends Component{
  render () {
    const { setToggle } = this.props;
    return(
      <section className="info-section" id="features" onClick={() => {setToggle(false)}}>
        <div className = "info-section-cardlist">
          <FeatureCard
            title = "Dark Mode"
            subtitle = "Welcome to the dark side..."
            gif ={darkModeGIF}
            jpg={darkModeJPG}
          />
          <FeatureCard
            title = "Save your Circuits"
            subtitle = "Set up once, use as much as you want"
            gif ={savedCircuitsGIF}
            jpg={savedCircuitsJPG}
          />
          <FeatureCard
            title = "Classic Stopwatch"
            subtitle = "Use Chrono as your all in one"
            gif = {stopwatchGIF}
            jpg= {stopwatchJPG}
          />
        </div>
      </section>
      
    )
  }
}

export default Features;