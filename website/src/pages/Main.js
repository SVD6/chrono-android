import Landing from './Landing';
import Footer from '../components/Footer';
import ContactUs from './ContactUs';
import Features from './Features';
import Navigation from '../components/Navigation';
import { RiMenu5Line } from "react-icons/ri";
import React, { useState, useEffect } from 'react';

function Main() {
    const [toggle, setToggle] = useState(false);
    useEffect(() => {
      console.log(toggle);
      {toggle?   
        document.getElementById("navigation").style.setProperty("display","block")
        :  
        document.getElementById("navigation").style.setProperty("display","none")};
      return () => {
      }
    }, [toggle])

    return (
    <>
        <button className="nav-toggle" onClick={()=>setToggle(!toggle)}>
            <RiMenu5Line size={30} fill={'lavender'}/>
        </button>
        <Navigation setToggle={setToggle}/>
        <Landing setToggle={setToggle}></Landing>
        <Features setToggle={setToggle}/>
        <ContactUs setToggle={setToggle}/>
        <Footer setToggle={setToggle}></Footer >
    </>
    )
}

export default Main; 