import React from 'react'
import logo from '../assets/logo.svg';
import circuitLight from '../assets/app-circuit-light.svg';

function Landing(props){
    const { setToggle } = props;

    return <header className="cover" id="home" onClick={() => setToggle(false)}>
        <section className="cover-content">
            <img src={logo} className="cover-logo" alt="logo" />
            <p className="tagline">custom circuit timers for your COVID shred</p>
            <a href='https://play.google.com/store/apps/details?id=ca.chronofit.chrono'><button className="download-button"> Download Now</button></a>
        </section>
        <section className="cover-image">
            <img src={circuitLight} className="cover-screenshot"/>
        </section>
    </header>
}

export default Landing;